package pl.edu.pwr.pkuchnowski.doryw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;
import pl.edu.pwr.pkuchnowski.doryw.domain.RequestContext;
import pl.edu.pwr.pkuchnowski.doryw.domain.Response;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.UserProfileResponse;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.User;
import pl.edu.pwr.pkuchnowski.doryw.dtorequest.UserRequest;
import pl.edu.pwr.pkuchnowski.doryw.dtorequest.patch.UserPatchRequest;
import pl.edu.pwr.pkuchnowski.doryw.entities.UserCredentialsEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.UserEntity;
import pl.edu.pwr.pkuchnowski.doryw.repositories.UserCredentialsRepository;
import pl.edu.pwr.pkuchnowski.doryw.services.EmployerService;
import pl.edu.pwr.pkuchnowski.doryw.services.InvalidatedTokenService;
import pl.edu.pwr.pkuchnowski.doryw.services.JwtService;
import pl.edu.pwr.pkuchnowski.doryw.services.UserService;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Map;

import static java.util.Collections.EMPTY_MAP;
import static java.util.Collections.emptyMap;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static pl.edu.pwr.pkuchnowski.doryw.enumeration.TokenType.ACCESS;
import static pl.edu.pwr.pkuchnowski.doryw.enumeration.TokenType.REFRESH;
import static pl.edu.pwr.pkuchnowski.doryw.utils.RequestUtils.getResponse;
import static pl.edu.pwr.pkuchnowski.doryw.utils.UserUtils.*;
import static pl.edu.pwr.pkuchnowski.doryw.utils.UserUtils.fromUserEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;
    private final EmployerService employerService;
    private final UserCredentialsRepository userCredentialsRepository;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final InvalidatedTokenService invalidatedTokenService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid UserRequest user, HttpServletRequest request) {
        String verificationUrl = request.getHeader("origin") + user.getVerificationUrl();
        userService.createUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getBirthDate(), verificationUrl);
        return ResponseEntity.created(getUri()).body(getResponse(request, emptyMap(), "Konto zostało utworzone. Sprawdź swoją skrzynkę mailową, aby aktywować konto.", HttpStatus.CREATED));
    }

    @PostMapping("/logout")
    public void logoutUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityContextHolder.clearContext();

        String accessToken = jwtService.extractToken(request, ACCESS).orElse(null);
        String refreshToken = jwtService.extractToken(request, REFRESH).orElse(null);

        try{
            Date expirationDate = jwtService.getExpirationDate(accessToken);
            invalidatedTokenService.invalidateToken(accessToken, expirationDate);
        }catch (Exception e){
            if(e instanceof ExpiredJwtException){
                log.info("Access token expired, no need to invalidate");
            }
            log.error("Error while invalidating access token: {}", e.getMessage(), e);
        }

        try{
            Date expirationDate = jwtService.getExpirationDate(refreshToken);
            invalidatedTokenService.invalidateToken(refreshToken, expirationDate);
        }catch (Exception e){
            if(e instanceof ExpiredJwtException){
                log.info("Refresh token expired, no need to invalidate");
            }
            log.error("Error while invalidating refresh token: {}", e.getMessage(), e);
        }

        jwtService.removeCookie(request, response, ACCESS);
        jwtService.removeCookie(request, response, REFRESH);

        Response returnResponse = getResponse(request, EMPTY_MAP, "User logged out", HttpStatus.OK);
        response.setContentType(APPLICATION_JSON_VALUE);

        var out = response.getOutputStream();
        objectMapper.writeValue(out, returnResponse);
        out.flush();

    }

    @PostMapping("/verify-account")
    public ResponseEntity<Response> verifyUserAccount(@RequestParam("token") String token, HttpServletRequest request) {
        userService.verifyAccountToken(token);
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Konto zostało zweryfikowane!.", HttpStatus.OK));
    }

    @GetMapping("")
    public ResponseEntity<Response> getAuthenticatedUser(HttpServletRequest request) {
        UserEntity user = RequestContext.getUser();
        if (user.getId() != 0) {
            User userDto = fromUserEntity(user);
            if(userCredentialsRepository.getUserCredentialsByUserEntityReferenceId(user.getReferenceId()).isPresent()) {
                UserCredentialsEntity userCredentials = userCredentialsRepository.getUserCredentialsByUserEntityReferenceId(user.getReferenceId()).get();
                userDto.setCredentialsNonExpired(isCredentialsNonExpired(userCredentials));
            }
            userDto.setIsEmployer(employerService.isEmployer(user.getReferenceId()));
            return ResponseEntity.ok().body(getResponse(request, Map.of("user", userDto), "User found", HttpStatus.OK));
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("")
    public ResponseEntity<Response> updateUser(@RequestBody @Valid UserPatchRequest userRequest, HttpServletRequest request) {
        try {
            UserEntity user = userService.updateUser(
                    RequestContext.getUserId(),
                    userRequest.getFirstName(),
                    userRequest.getLastName(),
                    userRequest.getEmail(),
                    userRequest.getPhoneNumber(),
                    userRequest.getBirthDate()
            );
            UserProfileResponse updatedUser = new UserProfileResponse();
            BeanUtils.copyProperties(user, updatedUser);
            return ResponseEntity.ok().body(getResponse(request, Map.of("user", updatedUser), "User updated successfully.", HttpStatus.OK));
        } catch (TransactionSystemException ex) {
            Throwable rootCause = ex.getRootCause();
            log.error("TransactionSystemException in controller: {}", ex.getMessage(), ex);
            if (rootCause != null) {
                log.error("Root cause: {}", rootCause.getMessage(), rootCause);
            }
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error in controller: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @DeleteMapping("")
    public ResponseEntity<Response> deleteUser(HttpServletRequest request) {
        userService.deleteUser(RequestContext.getUserId());
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Konto zostało usunięte.", HttpStatus.OK));
    }

    private URI getUri() {
        return URI.create("");
    }

}
