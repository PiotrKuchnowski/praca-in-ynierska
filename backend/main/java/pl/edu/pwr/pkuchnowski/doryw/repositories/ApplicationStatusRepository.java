package pl.edu.pwr.pkuchnowski.doryw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pwr.pkuchnowski.doryw.entities.ApplicationStatusEntity;

@Repository
public interface ApplicationStatusRepository extends JpaRepository<ApplicationStatusEntity, Integer> {
    ApplicationStatusEntity findByName(String name);
}
