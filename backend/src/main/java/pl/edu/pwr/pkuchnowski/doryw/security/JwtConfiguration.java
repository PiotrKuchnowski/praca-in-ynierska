package pl.edu.pwr.pkuchnowski.doryw.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class JwtConfiguration {
    @Value("${jwt.access.token.validity}")
    private int accessTokenValidity;
    @Value("${jwt.refresh.token.validity}")
    private int refreshTokenValidity;
    @Value("${jwt.secret}")
    private String secret;
}
