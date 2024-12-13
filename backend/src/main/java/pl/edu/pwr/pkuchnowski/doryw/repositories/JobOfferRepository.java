package pl.edu.pwr.pkuchnowski.doryw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pwr.pkuchnowski.doryw.entities.JobOfferEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOfferEntity, Long> {
    Optional<JobOfferEntity> findJobOfferEntitiesByReferenceId(String jobOfferReferenceId);
    Optional<List<JobOfferEntity>> findAllJobOfferEntitiesByEmployer_Id(Long employerId);

    void deleteByReferenceId(String referenceId);

    Boolean existsByReferenceId(String referenceId);
}
