package pl.edu.pwr.pkuchnowski.doryw.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.pwr.pkuchnowski.doryw.domain.RequestContext;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.JobOfferResponse;
import pl.edu.pwr.pkuchnowski.doryw.dtorequest.JobOfferRequest;
import pl.edu.pwr.pkuchnowski.doryw.entities.*;
import pl.edu.pwr.pkuchnowski.doryw.repositories.JobOfferRepository;
import pl.edu.pwr.pkuchnowski.doryw.repositories.LocationRepository;
import pl.edu.pwr.pkuchnowski.doryw.repositories.PayTypeRepository;
import pl.edu.pwr.pkuchnowski.doryw.services.EmployerService;
import pl.edu.pwr.pkuchnowski.doryw.services.JobCategoryService;
import pl.edu.pwr.pkuchnowski.doryw.services.JobOfferService;
import pl.edu.pwr.pkuchnowski.doryw.utils.JobOfferUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.edu.pwr.pkuchnowski.doryw.utils.JobOfferUtils.fromJobOfferEntity;
import static pl.edu.pwr.pkuchnowski.doryw.utils.JobOfferUtils.fromJobOfferRequest;
import static pl.edu.pwr.pkuchnowski.doryw.utils.LocationUtils.addNonExistingLocations;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class JobOfferServiceImpl implements JobOfferService {
    private final JobOfferRepository jobOfferRepository;
    private final PayTypeRepository payTypeRepository;
    private final LocationRepository locationRepository;
    private final EmployerService employerService;
    private final JobCategoryService jobCategoryService;

    @Override
    public List<JobOfferResponse> getAllJobOffers() {
        List<JobOfferEntity> jobOfferEntities = jobOfferRepository.findAll();
        return jobOfferEntities.stream().map(JobOfferUtils::fromJobOfferEntity).collect(Collectors.toList());
    }

    @Override
    public JobOfferResponse getJobOfferResponseByReferenceId(String referenceId) {
        JobOfferEntity jobOfferEntity = jobOfferRepository.findJobOfferEntitiesByReferenceId(referenceId)
                        .orElseThrow(
                                () -> new IllegalArgumentException("Job offer with id: " + referenceId + " not found")
                        );
        return fromJobOfferEntity(jobOfferEntity);
    }

    @Override
    public void createJobOffer(JobOfferRequest jobOffer) {
        JobOfferEntity jobOfferEntity = fromJobOfferRequest(jobOffer);

        Set<LocationEntity> locationsEntitySet = addNonExistingLocations(jobOffer, locationRepository);
        jobOfferEntity.setLocations(locationsEntitySet);

        PayTypeEntity payTypeEntity = payTypeRepository.findByName(jobOffer.getPayType().getName())
                .orElseThrow(() -> new IllegalArgumentException("Pay type not found"));

        jobOfferEntity.setPayType(payTypeEntity);

        EmployerEntity employerEntity = employerService.getEmployerEntityByUserId(RequestContext.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Employer not found"));
        jobOfferEntity.setEmployer(employerEntity);

        JobCategoryEntity jobCategoryEntity = jobCategoryService.getJobCategoryEntityByName(jobOffer.getJobCategory());
        jobOfferEntity.setJobCategory(jobCategoryEntity);


        jobOfferRepository.save(jobOfferEntity);
    }

    @Override
    public List<JobOfferResponse> getAllJobOffersForEmployer(Long employerId) {
        Optional<List<JobOfferEntity>> jobOfferEntities = jobOfferRepository.findAllJobOfferEntitiesByEmployer_Id(employerId);
        if(jobOfferEntities.isEmpty()) {
            throw new IllegalArgumentException("No job offers for employer with id: " + employerId);
        }
        return jobOfferEntities.get().stream().map(JobOfferUtils::fromJobOfferEntity).collect(Collectors.toList());
    }

    @Override
    public JobOfferEntity getJobOfferEntityByReferenceId(String referenceId) {
        return jobOfferRepository.findJobOfferEntitiesByReferenceId(referenceId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Job offer with id: " + referenceId + " not found")
                );
    }

    @Override
    public void updateJobOffer(String jobOfferReferenceId, JobOfferRequest jobOfferRequest) {
        JobOfferEntity jobOfferEntity = getJobOfferEntityByReferenceId(jobOfferReferenceId);
        Set<LocationEntity> locationsEntitySet = addNonExistingLocations(jobOfferRequest, locationRepository);
        jobOfferEntity.setStartDate(LocalDateTime.parse(jobOfferRequest.getStartDate()));
        if(!jobOfferRequest.getEndDate().isEmpty()){
            jobOfferEntity.setEndDate(LocalDateTime.parse(jobOfferRequest.getEndDate()));
        }else{
            jobOfferEntity.setEndDate(null);
        }
        PayTypeEntity payTypeEntity = payTypeRepository.findByName(jobOfferRequest.getPayType().getName())
                .orElseThrow(() -> new IllegalArgumentException("Pay type not found"));
        jobOfferEntity.setDescription(jobOfferRequest.getDescription());
        jobOfferEntity.setTitle(jobOfferRequest.getTitle());
        jobOfferEntity.setPay(jobOfferRequest.getPay());
        jobOfferEntity.setPayType(payTypeEntity);
        jobOfferEntity.setLocations(locationsEntitySet);
        jobOfferEntity.setEmployer(
                employerService.getEmployerEntityByUserId(
                        RequestContext.getUser().getId()
                ).orElseThrow(() -> new IllegalArgumentException("Employer not found"))
        );
        jobOfferEntity.setJobCategory(jobCategoryService.getJobCategoryEntityByName(jobOfferRequest.getJobCategory()));
        jobOfferRepository.save(jobOfferEntity);

    }

    @Override
    public void deleteJobOffer(String id) {
        try {
            jobOfferRepository.deleteByReferenceId(id);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new IllegalArgumentException("Could not delete job offer");
        }
    }
}
