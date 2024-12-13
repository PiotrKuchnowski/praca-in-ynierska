package pl.edu.pwr.pkuchnowski.doryw.services;

import java.util.Date;

public interface InvalidatedTokenService {
    void invalidateToken(String token, Date expirationDate);
    void deleteExpiredTokens();
    boolean wasTokenInvalidatedBefore(String token);
    void invalidateAllTokensRelatedToUser(Long userId);
}
