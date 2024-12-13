package pl.edu.pwr.pkuchnowski.doryw.services;

import jakarta.validation.Valid;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.JobOfferResponse;
import pl.edu.pwr.pkuchnowski.doryw.dtorequest.JobOfferRequest;
import pl.edu.pwr.pkuchnowski.doryw.entities.JobOfferEntity;

import java.util.List;

public interface JobOfferService {
    List<JobOfferResponse> getAllJobOffers();
    JobOfferResponse getJobOfferResponseByReferenceId(String id);
    void createJobOffer(JobOfferRequest jobOffer);
    List<JobOfferResponse> getAllJobOffersForEmployer(Long employerId);

    JobOfferEntity getJobOfferEntityByReferenceId(String jobOfferReferenceId);

    void updateJobOffer(String jobOfferId, @Valid JobOfferRequest jobOfferRequest);

    void deleteJobOffer(String id);
}

