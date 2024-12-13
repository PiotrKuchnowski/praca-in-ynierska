package pl.edu.pwr.pkuchnowski.doryw.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.pwr.pkuchnowski.doryw.domain.ApiAuthentication;
import pl.edu.pwr.pkuchnowski.doryw.domain.UserPrincipal;
import pl.edu.pwr.pkuchnowski.doryw.exception.ApiException;
import pl.edu.pwr.pkuchnowski.doryw.services.UserService;

import java.util.function.Consumer;
import java.util.function.Function;

import static pl.edu.pwr.pkuchnowski.doryw.utils.UserUtils.isCredentialsNonExpired;


@Component
@RequiredArgsConstructor
public class ApiAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var apiAuthentication = authenticationFunction.apply(authentication);
        var user = userService.getUserByEmail(apiAuthentication.getEmail());
        if(user != null) {
            var userCredential = userService.getUserCredentialByReferenceId(user.getReferenceId());
            if(!isCredentialsNonExpired(userCredential)) {
                throw new ApiException("Password expired");
            }
            user.setCredentialsNonExpired(true);
            var userPrincipal = new UserPrincipal(user, userCredential);
            validAccount.accept(userPrincipal);
            if(encoder.matches(apiAuthentication.getPassword(), userCredential.getPassword())) {
               return ApiAuthentication.authenticated(user);
            } else throw new BadCredentialsException("Niepoprawne dane logowania");
        } else throw new UsernameNotFoundException("Nie znaleziono użytkownika");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiAuthentication.class.isAssignableFrom(authentication);
    }

    private final Function<Authentication, ApiAuthentication> authenticationFunction = authentication -> (ApiAuthentication) authentication;

    private final Consumer<UserPrincipal> validAccount = userPrincipal -> {
        if (!userPrincipal.isAccountNonLocked()) {
            throw new LockedException("Konto zablokowane");
        }
        if (!userPrincipal.isEnabled()) {
            throw new DisabledException("Konto nieaktywne");
        }
        if (!userPrincipal.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("Dane logowania wygasły");
        }
        if (!userPrincipal.isAccountNonExpired()) {
            throw new DisabledException("Konto wygasło.");
        }
    };
}
