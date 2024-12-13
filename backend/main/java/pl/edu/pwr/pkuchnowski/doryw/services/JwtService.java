package pl.edu.pwr.pkuchnowski.doryw.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import pl.edu.pwr.pkuchnowski.doryw.domain.Token;
import pl.edu.pwr.pkuchnowski.doryw.domain.TokenData;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.User;
import pl.edu.pwr.pkuchnowski.doryw.enumeration.TokenType;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

public interface JwtService {
    String createToken(User user, Function<Token, String> tokenFunction);
    Optional<String> extractToken(HttpServletRequest request, TokenType tokenType);
    void addCookie(HttpServletResponse response, User user, TokenType type);
    <T> T getTokenData(String token, Function<TokenData, T> tokenFunction) throws ParseException;
    void removeCookie(HttpServletRequest request, HttpServletResponse response, TokenType type);
    Authentication getAuthentication(TokenData tokenData);
    Long getUserId(String token);
    Date getExpirationDate(String token);
}
