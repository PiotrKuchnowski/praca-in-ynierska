package pl.edu.pwr.pkuchnowski.doryw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pwr.pkuchnowski.doryw.entities.EmployerEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.UserEntity;

import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<EmployerEntity, Long> {
    Optional<EmployerEntity> findByUserEntity(UserEntity userEntity);
    Optional<EmployerEntity> findByUserEntityReferenceId(String employerReferenceId);
    Optional<EmployerEntity> findByUserEntityId(Long userId);

    EmployerEntity getByUserEntity(UserEntity userEntity);
    Boolean existsByUserEntityId(Long userId);
    boolean existsByUserEntity(UserEntity userEntity);
}
