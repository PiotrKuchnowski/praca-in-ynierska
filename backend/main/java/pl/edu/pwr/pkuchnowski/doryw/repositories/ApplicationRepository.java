package pl.edu.pwr.pkuchnowski.doryw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pwr.pkuchnowski.doryw.entities.ApplicationEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.ApplicationStatusEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.JobOfferEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Integer> {
    List<ApplicationEntity> findAllByUser(UserEntity userEntity);
    List<ApplicationEntity> findAllByJobOfferAndStatus(JobOfferEntity jobOffer, ApplicationStatusEntity status1);

    List<ApplicationEntity> findAllByJobOfferAndStatusNotAndStatusNot(JobOfferEntity jobOffer, ApplicationStatusEntity status1, ApplicationStatusEntity status2);
    List<ApplicationEntity> findAllByUserAndStatusNot(UserEntity user, ApplicationStatusEntity status);
    Boolean existsByUserIdAndJobOfferId(Long userId, Long jobOfferId);
    Boolean existsByUserIdAndJobOfferIdAndStatusNot(Long userId, Long jobOfferId, ApplicationStatusEntity status);
    void deleteByReferenceId(String referenceId);
    Optional<ApplicationEntity> getApplicationEntitiesByReferenceId(String referenceId);
    List<ApplicationEntity> findAllByUserAndStatus(UserEntity user, ApplicationStatusEntity status);

    ApplicationEntity findByUserAndJobOffer(UserEntity userEntity, JobOfferEntity jobOfferEntity);
}
