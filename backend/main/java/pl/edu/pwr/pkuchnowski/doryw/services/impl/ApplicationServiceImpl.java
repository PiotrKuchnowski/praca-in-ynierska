package pl.edu.pwr.pkuchnowski.doryw.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.pwr.pkuchnowski.doryw.domain.RequestContext;
import pl.edu.pwr.pkuchnowski.doryw.dtorequest.ApplicationRequest;
import pl.edu.pwr.pkuchnowski.doryw.dtorequest.ApplicationResponseRequest;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.ApplicationResponse;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.JobOfferResponse;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.UserProfileResponse;
import pl.edu.pwr.pkuchnowski.doryw.entities.ApplicationEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.ApplicationStatusEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.JobOfferEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.UserEntity;
import pl.edu.pwr.pkuchnowski.doryw.repositories.ApplicationRepository;
import pl.edu.pwr.pkuchnowski.doryw.repositories.ApplicationStatusRepository;
import pl.edu.pwr.pkuchnowski.doryw.services.ApplicationService;
import pl.edu.pwr.pkuchnowski.doryw.services.JobOfferService;
import pl.edu.pwr.pkuchnowski.doryw.services.UserService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobOfferService jobOfferService;
    private final UserService userService;
    private final ApplicationStatusRepository applicationStatusRepository;

    @Override
    public void createApplication(ApplicationRequest applicationRequest) {
        checkIfAlreadyApplied(applicationRequest);
        ApplicationEntity applicationEntity = new ApplicationEntity();
        JobOfferEntity jobOfferEntity = jobOfferService.getJobOfferEntityByReferenceId(applicationRequest.getJobOfferReferenceId());
        applicationEntity.setJobOffer(jobOfferEntity);
        Long userId = RequestContext.getUserId();
        if (Objects.equals(userId, jobOfferEntity.getEmployer().getUserEntity().getId())) {
            throw new IllegalArgumentException("Hmm... Wygląda na to, że próbujesz zaaplikować na swoje własne ogłoszenie. Gratulujemy przedsiębiorczości, ale to już zakrawa na definicję samozatrudnienia!");
        }
        UserEntity userEntity = userService.getUserEntityById(userId);
        applicationEntity.setUser(userEntity);
        applicationEntity.setMessage(applicationRequest.getMessage());
        applicationEntity.setStatus(applicationStatusRepository.findByName("Zaaplikowano"));
        applicationRepository.save(applicationEntity);
    }

    @Override
    public List<ApplicationResponse> getApplications(String jobOfferReferenceId) {
        JobOfferEntity jobOfferEntity = jobOfferService.getJobOfferEntityByReferenceId(jobOfferReferenceId);
        List<ApplicationEntity> applicationEntities = applicationRepository.findAllByJobOfferAndStatus(jobOfferEntity, applicationStatusRepository.findByName("Zaaplikowano"));
        return getResponse(applicationEntities);
    }

    @Override
    public ApplicationResponse getApplicationForUserAndJob(Long userId, String jobOfferId) {

        JobOfferEntity jobOfferEntity = jobOfferService.getJobOfferEntityByReferenceId(jobOfferId);
        UserEntity userEntity = userService.getUserEntityById(userId);
        ApplicationEntity applicationEntity = applicationRepository.findByUserAndJobOffer(userEntity, jobOfferEntity);
        return getApplicationResponse(userEntity, applicationEntity);
    }

    private ApplicationResponse getApplicationResponse(UserEntity userEntity, ApplicationEntity applicationEntity) {
        ApplicationResponse applicationResponse = new ApplicationResponse();
        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setEmail(userEntity.getEmail());
        userProfileResponse.setFirstName(userEntity.getFirstName());
        userProfileResponse.setLastName(userEntity.getLastName());
        userProfileResponse.setPhoneNumber(userEntity.getPhoneNumber());
        applicationResponse.setUser(userProfileResponse);
        applicationResponse.setApplicationId(applicationEntity.getReferenceId());
        applicationResponse.setMessage(applicationEntity.getMessage());
        applicationResponse.setStatus(applicationEntity.getStatus().getName());
        applicationResponse.setResponse(applicationEntity.getResponse());
        return applicationResponse;
    }

    @Override
    public List<ApplicationResponse> getEmploymentApplications(String jobOfferReferenceId) {
        JobOfferEntity jobOfferEntity = jobOfferService.getJobOfferEntityByReferenceId(jobOfferReferenceId);
        List<ApplicationEntity> applicationEntities = applicationRepository.findAllByJobOfferAndStatus(jobOfferEntity, applicationStatusRepository.findByName("Przyjęto akceptację"));
        return getResponse(applicationEntities);
    }

    private List<ApplicationResponse> getResponse(List<ApplicationEntity> applicationEntities) {
        return applicationEntities.stream().map(applicationEntity -> {
            UserEntity userEntity = applicationEntity.getUser();
            return getApplicationResponse(userEntity, applicationEntity);
        }).toList();
    }

    @Override
    public List<JobOfferResponse> getJobOffersUserAppliedFor(Long userId) {
        UserEntity userEntity = userService.getUserEntityById(userId);
        List<ApplicationEntity> applicationEntities = applicationRepository.findAllByUserAndStatusNot(userEntity, applicationStatusRepository.findByName("Przyjęto akceptację"));
        return applicationEntities.stream().map(applicationEntity -> {
            JobOfferEntity jobOfferEntity = applicationEntity.getJobOffer();
            return jobOfferService.getJobOfferResponseByReferenceId(jobOfferEntity.getReferenceId());
        }).toList();
    }

    @Override
    public List<JobOfferResponse> getJobOffersUserAccepted(Long userId) {
        UserEntity userEntity = userService.getUserEntityById(userId);
        List<ApplicationEntity> applicationEntities = applicationRepository.findAllByUserAndStatus(userEntity, applicationStatusRepository.findByName("Przyjęto akceptację"));
        return applicationEntities.stream().map(applicationEntity -> {
            JobOfferEntity jobOfferEntity = applicationEntity.getJobOffer();
            return jobOfferService.getJobOfferResponseByReferenceId(jobOfferEntity.getReferenceId());
        }).toList();
    }

    @Override
    public void updateApplication(String applicationId, ApplicationResponseRequest applicationResponseRequest) {
        Optional<ApplicationEntity> applicationEntity = applicationRepository.getApplicationEntitiesByReferenceId(applicationId);
        if (applicationEntity.isEmpty()) {
            throw new IllegalArgumentException("Nie znaleziono zainteresowania");
        }
        ApplicationStatusEntity applicationStatusEntity;
        if (applicationResponseRequest.getAccepted()) {
            applicationStatusEntity = applicationStatusRepository.findByName("Zaakceptowano");
        } else {
            applicationStatusEntity = applicationStatusRepository.findByName("Odrzucono");
        }
        applicationEntity.get().setStatus(applicationStatusEntity);
        applicationEntity.get().setResponse(applicationResponseRequest.getResponse());
        applicationRepository.save(applicationEntity.get());
    }

    @Override
    public void acknowledgeAcceptance(String referenceId) {
        Optional<ApplicationEntity> applicationEntity = applicationRepository.getApplicationEntitiesByReferenceId(referenceId);
        if (applicationEntity.isEmpty()) {
            throw new IllegalArgumentException("Nie znaleziono zainteresowania");
        }
        ApplicationStatusEntity applicationStatusEntity = applicationStatusRepository.findByName("Przyjęto akceptację");
        applicationEntity.get().setStatus(applicationStatusEntity);
        applicationRepository.save(applicationEntity.get());
    }


    @Override
    public void deleteApplication(String id) {
        try {
            applicationRepository.deleteByReferenceId(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Nie udało się usunąć zainteresowania");
        }

    }

    private void checkIfAlreadyApplied(ApplicationRequest applicationRequest) {
        JobOfferEntity jobOfferEntity = jobOfferService.getJobOfferEntityByReferenceId(applicationRequest.getJobOfferReferenceId());
        if (applicationRepository.existsByUserIdAndJobOfferId(RequestContext.getUserId(), jobOfferEntity.getId())) {
            throw new IllegalArgumentException("Już aplikowałeś na to ogłoszenie");
        }
    }


}
