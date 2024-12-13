package pl.edu.pwr.pkuchnowski.doryw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pwr.pkuchnowski.doryw.entities.JobCategoryEntity;

import java.util.Optional;

@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategoryEntity, Long> {
    Optional<JobCategoryEntity> findByName(String name);
}
