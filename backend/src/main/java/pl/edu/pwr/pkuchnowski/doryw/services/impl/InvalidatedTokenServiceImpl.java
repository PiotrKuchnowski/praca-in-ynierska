package pl.edu.pwr.pkuchnowski.doryw.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pwr.pkuchnowski.doryw.entities.InvalidatedTokenEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.UserEntity;
import pl.edu.pwr.pkuchnowski.doryw.repositories.InvalidatedTokenRepository;
import pl.edu.pwr.pkuchnowski.doryw.repositories.UserRepository;
import pl.edu.pwr.pkuchnowski.doryw.services.InvalidatedTokenService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class InvalidatedTokenServiceImpl implements InvalidatedTokenService {

    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final UserRepository userRepository;

    @Override
    public void invalidateToken(String token, Date expirationDate) {
        InvalidatedTokenEntity invalidatedTokenEntity = new InvalidatedTokenEntity();
        invalidatedTokenEntity.setToken(token);
        invalidatedTokenEntity.setExpirationDate(expirationDate);
        invalidatedTokenRepository.save(invalidatedTokenEntity);
    }

    @Override
    public void deleteExpiredTokens() {
        invalidatedTokenRepository.deleteByExpirationDateBefore(new Date());
    }

    @Override
    public boolean wasTokenInvalidatedBefore(String token) {
        return invalidatedTokenRepository.existsByToken(token);
    }

    @Override
    public void invalidateAllTokensRelatedToUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow();
        userEntity.setLastRefreshTokenInvalidation(new Date());
        userRepository.save(userEntity);
    }
}
