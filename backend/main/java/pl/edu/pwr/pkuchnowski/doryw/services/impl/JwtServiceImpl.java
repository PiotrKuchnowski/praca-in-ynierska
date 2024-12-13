package pl.edu.pwr.pkuchnowski.doryw.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.function.TriConsumer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import pl.edu.pwr.pkuchnowski.doryw.domain.Token;
import pl.edu.pwr.pkuchnowski.doryw.domain.TokenData;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.User;
import pl.edu.pwr.pkuchnowski.doryw.enumeration.TokenType;
import pl.edu.pwr.pkuchnowski.doryw.security.JwtConfiguration;
import pl.edu.pwr.pkuchnowski.doryw.services.InvalidatedTokenService;
import pl.edu.pwr.pkuchnowski.doryw.services.JwtService;
import pl.edu.pwr.pkuchnowski.doryw.services.UserService;

import javax.crypto.SecretKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.jsonwebtoken.Header.JWT_TYPE;
import static io.jsonwebtoken.Header.TYPE;
import static io.jsonwebtoken.SignatureAlgorithm.*;
import static java.util.Arrays.stream;
import static java.util.Date.*;
import static java.util.Optional.empty;
import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;
import static pl.edu.pwr.pkuchnowski.doryw.constants.Constants.*;
import static pl.edu.pwr.pkuchnowski.doryw.enumeration.TokenType.ACCESS;
import static pl.edu.pwr.pkuchnowski.doryw.enumeration.TokenType.REFRESH;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl extends JwtConfiguration implements JwtService {

    private final UserService userService;

    private final Supplier<SecretKey> key = () -> Keys.hmacShaKeyFor(Decoders.BASE64.decode(getSecret()));

    private final Function<String, Claims> claimsFunction = token ->
            Jwts.parser()
                    .verifyWith(key.get())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

    private final Function<String, String> subject = token -> getClaimsValue(token, Claims::getSubject);

    private final BiFunction<HttpServletRequest, String, Optional<String>> extractToken = (request, cookieName) ->
            Optional.of(stream(request.getCookies() == null ? new Cookie[]{new Cookie(EMPTY_VALUE, EMPTY_VALUE)} : request.getCookies())
                            .filter(cookie -> Objects.equals(cookieName, cookie.getName()))
                            .map(Cookie::getValue)
                            .findAny())
                    .orElse(empty());

    private final BiFunction<HttpServletRequest, String, Optional<Cookie>> extractCookie = (request, cookieName) ->
            Optional.of(stream(request.getCookies() == null ? new Cookie[]{new Cookie(EMPTY_VALUE, EMPTY_VALUE)} : request.getCookies())
                            .filter(cookie -> Objects.equals(cookieName, cookie.getName()))
                            .findAny())
                    .orElse(empty());

    private final Supplier<JwtBuilder> builder = () ->
            Jwts.builder()
                    .header().add(Map.of(TYPE, JWT_TYPE))
                    .and()
                    .audience().add(DORYW)
                    .and()
                    .id(UUID.randomUUID().toString())
                    .issuedAt(from(Instant.now()))
                    .notBefore(new Date())
                    .signWith(key.get(), Jwts.SIG.HS512);

    private final BiFunction<User, TokenType, String> buildToken = (user, type) -> {
        String roles = user.getRoles().stream()
                .map(role -> "ROLE_" + role)
                .collect(Collectors.joining(","));
        return Objects.equals(type, ACCESS) ? builder.get()
                .subject(user.getReferenceId())
                .claim(AUTHORITIES, roles)
                .expiration(from(Instant.now().plusSeconds(getAccessTokenValidity())))
                .compact() : builder.get()
                .subject(user.getReferenceId())
                .expiration(from(Instant.now().plusSeconds(getRefreshTokenValidity())))
                .compact();
    };


    private final TriConsumer<HttpServletResponse, User, TokenType> addCookie = (response, user, type) -> {
        var accessToken = createToken(user, Objects.equals(type, ACCESS) ? Token::getAccess : Token::getRefresh);
        var cookie = new Cookie(type.getValue(), accessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Ensure this is set to true if using HTTPS
        if (Objects.equals(type, REFRESH)) {
            cookie.setMaxAge(getRefreshTokenValidity());
        } else {
            cookie.setMaxAge(getAccessTokenValidity());
        }
        cookie.setPath("/");
//        cookie.setAttribute("SameSite", NONE.getValue());
        response.addCookie(cookie);
    };
    private final InvalidatedTokenService invalidatedTokenService;

    private <T> T getClaimsValue(String token, Function<Claims, T> claims) {
        return claimsFunction.andThen(claims).apply(token);
    }

    public Function<String, List<GrantedAuthority>> authorities = token ->
            commaSeparatedStringToAuthorityList(new StringJoiner(AUTHORITY_DELIMITER)
                    .add(claimsFunction.apply(token).get(AUTHORITIES, String.class))
                    .add(ROLE_PREFIX + claimsFunction.apply(token).get(ROLE, String.class)).toString());

    @Override
    public String createToken(User user, Function<Token, String> tokenFunction) {
        var token = Token.builder()
                .access(buildToken.apply(user, ACCESS))
                .refresh(buildToken.apply(user, REFRESH))
                .build();
        return tokenFunction.apply(token);
    }

    @Override
    public Optional<String> extractToken(HttpServletRequest request, TokenType tokenType) {
        return extractToken.apply(request, tokenType.getValue());
    }

    @Override
    public void addCookie(HttpServletResponse response, User user, TokenType type) {
        addCookie.accept(response, user, type);
    }

    @Override
    public <T> T getTokenData(String token, Function<TokenData, T> tokenFunction) throws ParseException {
        boolean wasInvalidated = invalidatedTokenService.wasTokenInvalidatedBefore(token);
        if(wasInvalidated) {
            invalidatedTokenService.invalidateAllTokensRelatedToUser(getUserId(token));
            return tokenFunction.apply(TokenData.builder().valid(false).build());
        }
        Claims claims = claimsFunction.apply(token);
        Date issuedAt = claims.getIssuedAt();
        User user = userService.getUserByUserReferenceId(subject.apply(token));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date lastRefreshTokenInvalidation = formatter.parse(user.getLastRefreshTokenInvalidation());
        boolean isValid = Objects.equals(user.getReferenceId(), claims.getSubject()) &&
                !claims.getExpiration().before(new Date()) &&
                issuedAt.after(lastRefreshTokenInvalidation);
        return tokenFunction.apply(TokenData.builder()
                .valid(isValid)
                .authorities(authorities.apply(token))
                .claims(claims)
                .user(user)
                .build());
    }

    @Override
    public void removeCookie(HttpServletRequest request, HttpServletResponse response, TokenType type) {
        var optionalCookie = extractCookie.apply(request, type.getValue());
        if (optionalCookie.isPresent()) {
            var cookie = optionalCookie.get();
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // Ensure this is set to true if using HTTPS
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }

    @Override
    public Authentication getAuthentication(TokenData tokenData) {
        User userDetails = tokenData.getUser();
        return new UsernamePasswordAuthenticationToken(userDetails, null, tokenData.getAuthorities());
    }

    @Override
    public Long getUserId(String token) {
        String subject = claimsFunction.apply(token).getSubject();
        return userService.getUserIdByUserReferenceId(subject);
    }

    @Override
    public Date getExpirationDate(String token) {
        return claimsFunction.apply(token).getExpiration();
    }
}
