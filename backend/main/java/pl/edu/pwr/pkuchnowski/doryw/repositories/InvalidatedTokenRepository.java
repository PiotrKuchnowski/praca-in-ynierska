package pl.edu.pwr.pkuchnowski.doryw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pwr.pkuchnowski.doryw.entities.InvalidatedTokenEntity;

import java.util.Date;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedTokenEntity, Long> {
    boolean existsByToken(String token);
    void deleteByExpirationDateBefore(Date expirationDate);
}
