package pl.edu.pwr.pkuchnowski.doryw.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean(name = { "userLoginCache" })
    public CacheStore<String, Integer> userLoginCache() {
        return new CacheStore<>(900, TimeUnit.SECONDS);
    }
}
