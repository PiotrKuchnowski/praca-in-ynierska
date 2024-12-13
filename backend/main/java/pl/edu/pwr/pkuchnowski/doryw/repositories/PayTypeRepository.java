package pl.edu.pwr.pkuchnowski.doryw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pwr.pkuchnowski.doryw.entities.PayTypeEntity;

import java.util.Optional;

@Repository
public interface PayTypeRepository extends JpaRepository<PayTypeEntity, Long> {
    Optional<PayTypeEntity> findByName(String name);
}
