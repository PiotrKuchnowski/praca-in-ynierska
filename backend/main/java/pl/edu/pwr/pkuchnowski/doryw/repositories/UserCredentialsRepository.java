package pl.edu.pwr.pkuchnowski.doryw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pwr.pkuchnowski.doryw.entities.UserCredentialsEntity;

import java.util.Optional;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentialsEntity, Long> {
    Optional<UserCredentialsEntity> getUserCredentialsByUserEntityId(Long userId);
    Optional<UserCredentialsEntity> getUserCredentialsByUserEntityReferenceId(String userReferenceId);
}
