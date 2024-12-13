package pl.edu.pwr.pkuchnowski.doryw.utils;

import pl.edu.pwr.pkuchnowski.doryw.domain.RequestContext;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.JobCategoryResponse;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.JobOfferResponse;
import pl.edu.pwr.pkuchnowski.doryw.dtorequest.JobOfferRequest;
import pl.edu.pwr.pkuchnowski.doryw.entities.*;
import pl.edu.pwr.pkuchnowski.doryw.repositories.PayTypeRepository;
import pl.edu.pwr.pkuchnowski.doryw.repositories.UserCredentialsRepository;
import pl.edu.pwr.pkuchnowski.doryw.services.EmployerService;
import pl.edu.pwr.pkuchnowski.doryw.services.JobCategoryService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.edu.pwr.pkuchnowski.doryw.utils.EmployerUtils.fromEmployerEntity;
import static pl.edu.pwr.pkuchnowski.doryw.utils.LocationUtils.fromLocationEntities;
import static pl.edu.pwr.pkuchnowski.doryw.utils.PayTypeUtils.fromPayTypeEntity;

public class JobOfferUtils {
    public static JobOfferResponse fromJobOfferEntity(JobOfferEntity jobOfferEntity) {
        JobOfferResponse jobOffer = new JobOfferResponse();
        jobOffer.setJobOfferReferenceId(jobOfferEntity.getReferenceId());
        jobOffer.setEmployerID(fromEmployerEntity(jobOfferEntity.getEmployer()));
        jobOffer.setStartDate(jobOfferEntity.getStartDate().toString());
        LocalDateTime endDate = jobOfferEntity.getEndDate();
        if(endDate != null){
            jobOffer.setEndDate(endDate.toString());
        }
        jobOffer.setDescription(jobOfferEntity.getDescription());
        jobOffer.setTitle(jobOfferEntity.getTitle());
        jobOffer.setPay(jobOfferEntity.getPay());
        jobOffer.setPayType(fromPayTypeEntity(jobOfferEntity.getPayType()));
        Set<LocationEntity> locationEntities = jobOfferEntity.getLocations();
        jobOffer.setLocations(fromLocationEntities(locationEntities));
        jobOffer.setJobCategory(jobOfferEntity.getJobCategory().getName());
        return jobOffer;
    }

    public static JobOfferEntity fromJobOfferRequest(JobOfferRequest jobOfferRequest) {
        JobOfferEntity jobOfferEntity = new JobOfferEntity();
        jobOfferEntity.setStartDate(LocalDateTime.parse(jobOfferRequest.getStartDate()));
        if(!jobOfferRequest.getEndDate().isEmpty()){
            jobOfferEntity.setEndDate(LocalDateTime.parse(jobOfferRequest.getEndDate()));
        }
        jobOfferEntity.setDescription(jobOfferRequest.getDescription());
        jobOfferEntity.setTitle(jobOfferRequest.getTitle());
        jobOfferEntity.setPay(jobOfferRequest.getPay());
    return jobOfferEntity;
    }

    public static List<JobCategoryResponse> fromJobCategoryEntities(List<JobCategoryEntity> jobCategoryEntities) {
        return jobCategoryEntities.stream().map(entity -> {
            JobCategoryResponse jobCategory = new JobCategoryResponse();
            jobCategory.setName(entity.getName());
            return jobCategory;
        }).collect(Collectors.toList());
    }
}
