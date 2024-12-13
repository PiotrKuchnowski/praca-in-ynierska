package pl.edu.pwr.pkuchnowski.doryw.configuration;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.edu.pwr.pkuchnowski.doryw.services.InvalidatedTokenService;

import static pl.edu.pwr.pkuchnowski.doryw.constants.Constants.ONE_WEEK_IN_MILLIS;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class TokenCleaner {

    private final InvalidatedTokenService invalidatedTokenService;

    @Scheduled(fixedRate = ONE_WEEK_IN_MILLIS)
    @Transactional
    public void removeExpiredTokens() {
        System.out.println("Removing expired tokens from invalidated tokens records");
        invalidatedTokenService.deleteExpiredTokens();
    }
}
