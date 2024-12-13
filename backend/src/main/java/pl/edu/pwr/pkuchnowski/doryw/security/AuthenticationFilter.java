package pl.edu.pwr.pkuchnowski.doryw.security;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;
import pl.edu.pwr.pkuchnowski.doryw.domain.RequestContext;
import pl.edu.pwr.pkuchnowski.doryw.domain.TokenData;
import pl.edu.pwr.pkuchnowski.doryw.dtorequest.LoginRequest;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.User;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.UserLoginResponse;
import pl.edu.pwr.pkuchnowski.doryw.enumeration.TokenType;
import pl.edu.pwr.pkuchnowski.doryw.services.EmployerService;
import pl.edu.pwr.pkuchnowski.doryw.services.InvalidatedTokenService;
import pl.edu.pwr.pkuchnowski.doryw.services.JwtService;
import pl.edu.pwr.pkuchnowski.doryw.services.UserService;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.edu.pwr.pkuchnowski.doryw.constants.Constants.LOGIN_PATH;
import static pl.edu.pwr.pkuchnowski.doryw.constants.Constants.PATHS_WITHOUT_ROLES;
import static pl.edu.pwr.pkuchnowski.doryw.domain.ApiAuthentication.unauthenticated;
import static pl.edu.pwr.pkuchnowski.doryw.enumeration.LoginType.LOGIN_ATTEMPT;
import static pl.edu.pwr.pkuchnowski.doryw.enumeration.LoginType.LOGIN_SUCCESS;
import static pl.edu.pwr.pkuchnowski.doryw.enumeration.TokenType.ACCESS;
import static pl.edu.pwr.pkuchnowski.doryw.enumeration.TokenType.REFRESH;
import static pl.edu.pwr.pkuchnowski.doryw.utils.RequestUtils.getResponse;
import static pl.edu.pwr.pkuchnowski.doryw.utils.RequestUtils.handleErrorResponse;

@Slf4j
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    @Autowired
    private final UserService userService;
    @Autowired
    private final EmployerService employerService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final ObjectMapper objectMapper;
    @Autowired
    private final InvalidatedTokenService invalidatedTokenService;
    AntPathMatcher pathMatcher = new AntPathMatcher();

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, EmployerService employerService, JwtService jwtService, ObjectMapper objectMapper, InvalidatedTokenService invalidatedTokenService) {
        super(new AntPathRequestMatcher(LOGIN_PATH, POST.name()), authenticationManager);
        this.userService = userService;
        this.employerService = employerService;
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
        this.invalidatedTokenService = invalidatedTokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            var user = objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true)
                    .readValue(request.getInputStream(), LoginRequest.class);
            userService.updateLoginAttempt(user.getEmail(), LOGIN_ATTEMPT);
            var authentication = unauthenticated(user.getEmail(), user.getPassword());
            return getAuthenticationManager().authenticate(authentication);
        } catch (Exception e) {
            log.error(e.getMessage());
            handleErrorResponse(request, response, e);
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        var user = (User) authentication.getPrincipal();
        userService.updateLoginAttempt(user.getEmail(), LOGIN_SUCCESS);
        user.setIsEmployer(employerService.isEmployer(user.getReferenceId()));
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        BeanUtils.copyProperties(user, userLoginResponse);
        var httpResponse = sendResponse(request, response, user, userLoginResponse, "Logowanie zakończone sukcesem");
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(OK.value());
        var out = response.getOutputStream();
        objectMapper.writeValue(out, httpResponse);
        out.flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        handleErrorResponse(request, response, failed);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String origin = httpRequest.getHeader("Origin");
        String path = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();
        if (isPathWithoutRoles(path) && (method.equals(GET.name()) || path.contains("/user")) ) {
            super.doFilter(request, response, chain);
            return;
        }

        String accessToken = jwtService.extractToken(httpRequest, ACCESS).orElse(null);
        String refreshToken = jwtService.extractToken(httpRequest, REFRESH).orElse(null);

        try {
            if (accessToken != null &&
                    jwtService.getTokenData(accessToken, TokenData::isValid)
            ) {
                authenticateByToken(accessToken, httpRequest);
                chain.doFilter(request, response);
                return;
            }

        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                log.error("Access token expired");
                log.info("Attempting to refresh tokens");
            } else {
                log.error(e.getMessage());
                jwtService.removeCookie(httpRequest, httpResponse, ACCESS);
                return;
            }
        }
        try {
            if (refreshToken != null &&
                    jwtService.getTokenData(refreshToken, TokenData::isValid)
            ) {
                authenticateByToken(refreshToken, httpRequest);
                refreshTokens(httpRequest, httpResponse, accessToken, refreshToken);
            }else{
                jwtService.removeCookie(httpRequest, httpResponse, REFRESH);
//                handleErrorResponse(httpRequest, httpResponse, new AccessDeniedException("Refresh token expired"));
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error(e.getMessage());
            jwtService.removeCookie(httpRequest, httpResponse, ACCESS);
            jwtService.removeCookie(httpRequest, httpResponse, REFRESH);
            handleErrorResponse(httpRequest, httpResponse, e);
        }
    }

    private void authenticateByToken(String token, HttpServletRequest httpRequest) throws ParseException {
        var userId = jwtService.getUserId(token);
        RequestContext.setUser(userService.getUserEntityById(userId));
        TokenData tokenData = jwtService.getTokenData(token, Function.identity());
        var authentication = jwtService.getAuthentication(tokenData);
        ((UsernamePasswordAuthenticationToken) authentication).setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void refreshTokens(HttpServletRequest request, HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        var userId = RequestContext.getUserId();
        var user = userService.getUserById(userId);
        jwtService.removeCookie(request, response, ACCESS);
        jwtService.removeCookie(request, response, REFRESH);
        tryToInvalidateToken(accessToken, ACCESS);
        tryToInvalidateToken(refreshToken, REFRESH);
        var httpResponse = sendResponse(request, response, user, new UserLoginResponse(), "Odświeżenie tokenów zakończone sukcesem");
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(OK.value());
        var out = response.getOutputStream();
        objectMapper.writeValue(out, httpResponse);
        out.flush();
    }

    private void tryToInvalidateToken(String token, TokenType type){
        String message = type + " token expired: " + token + "No need to invalidate it.";
        try{
            Date expiration = jwtService.getExpirationDate(token);
            invalidatedTokenService.invalidateToken(token, expiration);
        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                log.info(message);
            } else {
                log.error("Error while invalidating token: {}", e.getMessage(), e);
            }
        }
    }

    private Object sendResponse(HttpServletRequest request, HttpServletResponse response, User user, UserLoginResponse userLoginResponse, String message) {
        jwtService.addCookie(response, user, ACCESS);
        jwtService.addCookie(response, user, REFRESH);
        return getResponse(request, Map.of("user", userLoginResponse), message, OK);
    }

    private boolean isPathWithoutRoles(String path) {
        return PATHS_WITHOUT_ROLES.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
}
