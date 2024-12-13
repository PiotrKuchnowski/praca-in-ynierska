package pl.edu.pwr.pkuchnowski.doryw.domain;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.User;
import pl.edu.pwr.pkuchnowski.doryw.exception.ApiException;

import java.util.Collection;

public class ApiAuthentication extends AbstractAuthenticationToken {
    private static final String EMAIL_PROTECTED = "[EMAIL PROTECTED]";
    private static final String PASSWORD_PROTECTED = "[PASSWORD PROTECTED]";
    private User user;
    @Getter
    private final String email;
    @Getter
    private final String password;
    private final boolean authenticated;

    private ApiAuthentication(String email, String password) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.email = email;
        this.password = password;
        this.authenticated = false;
    }

    private ApiAuthentication(User user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
        this.email = EMAIL_PROTECTED;
        this.password = PASSWORD_PROTECTED;
        this.authenticated = true;
    }

    public static ApiAuthentication unauthenticated(String email, String password) {
        return new ApiAuthentication(email, password);
    }

    public static ApiAuthentication authenticated(User user) {
        return new ApiAuthentication(user, AuthorityUtils.createAuthorityList(user.getRoles()));
    }

    @Override
    public Object getCredentials() {
        return PASSWORD_PROTECTED;
    }

    @Override
    public Object getPrincipal() {
        return this.user;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        throw new ApiException("Nie można zmienić stanu autoryzacji");
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

}
