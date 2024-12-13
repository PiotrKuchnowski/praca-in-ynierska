package pl.edu.pwr.pkuchnowski.doryw.services;

import pl.edu.pwr.pkuchnowski.doryw.dtorequest.ApplicationRequest;
import pl.edu.pwr.pkuchnowski.doryw.dtorequest.ApplicationResponseRequest;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.ApplicationResponse;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.JobOfferResponse;

import java.util.List;

public interface ApplicationService {
    void createApplication(ApplicationRequest applicationRequest);

    List<ApplicationResponse> getEmploymentApplications(String jobOfferReferenceId);

    List<JobOfferResponse> getJobOffersUserAppliedFor(Long userId);

    List<JobOfferResponse> getJobOffersUserAccepted(Long userId);

    void updateApplication(String applicationId, ApplicationResponseRequest applicationResponseRequest);

    void acknowledgeAcceptance(String referenceId);

    void deleteApplication(String id);
    List<ApplicationResponse> getApplications(String jobOfferReferenceId);

    ApplicationResponse getApplicationForUserAndJob(Long userId, String jobOfferId);
}
