package pl.edu.pwr.pkuchnowski.doryw.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pwr.pkuchnowski.doryw.domain.RequestContext;
import pl.edu.pwr.pkuchnowski.doryw.domain.Response;
import pl.edu.pwr.pkuchnowski.doryw.dtorequest.ApplicationRequest;
import pl.edu.pwr.pkuchnowski.doryw.dtorequest.ApplicationResponseRequest;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.ApplicationsJobOfferResponse;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.ApplicationResponse;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.JobOfferResponse;
import pl.edu.pwr.pkuchnowski.doryw.services.ApplicationService;
import pl.edu.pwr.pkuchnowski.doryw.services.EmployerService;
import pl.edu.pwr.pkuchnowski.doryw.services.JobOfferService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static pl.edu.pwr.pkuchnowski.doryw.utils.RequestUtils.getResponse;
import static pl.edu.pwr.pkuchnowski.doryw.utils.RequestUtils.handleErrorResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/applications")
@Slf4j
public class ApplicationsController {

    private final ApplicationService applicationService;
    private final JobOfferService jobOfferService;
    private final EmployerService employerService;

    @PostMapping("")
    public ResponseEntity<Response> createApplication(@RequestBody @Valid ApplicationRequest applicationRequest, HttpServletRequest request, HttpServletResponse response) {
        try{
            applicationService.createApplication(applicationRequest);
            return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Interest added", org.springframework.http.HttpStatus.OK));
        } catch (Exception e) {
            log.error(e.getMessage());
            handleErrorResponse(request, response, e);
            return null;
        }
    }

    @GetMapping("/employer")
    public ResponseEntity<Response> getInterestedUsers(HttpServletRequest request, HttpServletResponse response) {
        try{
            List<JobOfferResponse> jobOfferResponses = jobOfferService.getAllJobOffersForEmployer(employerService.getEmployerIdByUserId(RequestContext.getUserId()));
            List<ApplicationsJobOfferResponse> applicationsJobOfferRespons = new ArrayList<>();
            for (JobOfferResponse jobOfferResponse : jobOfferResponses) {
                List<ApplicationResponse> interest = applicationService.getApplications(jobOfferResponse.getJobOfferReferenceId());
                ApplicationsJobOfferResponse applicationsJobOfferResponse = new ApplicationsJobOfferResponse();
                applicationsJobOfferResponse.setJobOfferResponse(jobOfferResponse);
                applicationsJobOfferResponse.setApplications(interest);
                applicationsJobOfferRespons.add(applicationsJobOfferResponse);
            }
            return ResponseEntity.ok().body(getResponse(request, Map.of("interests", applicationsJobOfferRespons), "Interests fetched", org.springframework.http.HttpStatus.OK));
        } catch (Exception e) {
            log.error(e.getMessage());
            handleErrorResponse(request, response, e);
            return null;
        }
    }

    @GetMapping("/employer/accepted")
    public ResponseEntity<Response> getAcceptedUsers(HttpServletRequest request, HttpServletResponse response) {
        try{
            List<JobOfferResponse> jobOfferResponses = jobOfferService.getAllJobOffersForEmployer(employerService.getEmployerIdByUserId(RequestContext.getUserId()));
            List<ApplicationsJobOfferResponse> applicationsJobOfferRespons = new ArrayList<>();
            for (JobOfferResponse jobOfferResponse : jobOfferResponses) {
                List<ApplicationResponse> interest = applicationService.getEmploymentApplications(jobOfferResponse.getJobOfferReferenceId());
                ApplicationsJobOfferResponse applicationsJobOfferResponse = new ApplicationsJobOfferResponse();
                applicationsJobOfferResponse.setJobOfferResponse(jobOfferResponse);
                applicationsJobOfferResponse.setApplications(interest);
                applicationsJobOfferRespons.add(applicationsJobOfferResponse);
            }
            return ResponseEntity.ok().body(getResponse(request, Map.of("interests", applicationsJobOfferRespons), "Interests fetched", org.springframework.http.HttpStatus.OK));
        } catch (Exception e) {
            log.error(e.getMessage());
            handleErrorResponse(request, response, e);
            return null;
        }
    }

    @GetMapping("/job-offer/{jobOfferId}")
    public ResponseEntity<Response> getInterestedUsersForJob(HttpServletRequest request, @PathVariable("jobOfferId") String jobOfferId, HttpServletResponse response) {
        try{
            List<ApplicationResponse> interests = applicationService.getApplications(jobOfferId);
            return ResponseEntity.ok().body(getResponse(request, Map.of("interests", interests), "Interests fetched", org.springframework.http.HttpStatus.OK));
        } catch (Exception e) {
            log.error(e.getMessage());
            handleErrorResponse(request, response, e);
            return null;
        }
    }
    @GetMapping("/job-offer/{jobOfferId}/accepted")
    public ResponseEntity<Response> getAcceptedUsersForJob(HttpServletRequest request, @PathVariable("jobOfferId") String id, HttpServletResponse response) {
        try{
            List<ApplicationResponse> acceptedUsers = applicationService.getEmploymentApplications(id);
            return ResponseEntity.ok().body(getResponse(request, Map.of("accepted", acceptedUsers), "Accepted users fetched", org.springframework.http.HttpStatus.OK));
        } catch (Exception e) {
            log.error(e.getMessage());
            handleErrorResponse(request, response, e);
            return null;
        }
    }

    @GetMapping("/user")
    public ResponseEntity<Response> getJobsForUser(HttpServletRequest request, HttpServletResponse response) {
        try{
            List<JobOfferResponse> jobOffersUserAppliedFor = applicationService.getJobOffersUserAppliedFor(RequestContext.getUserId());
            return ResponseEntity.ok().body(getResponse(request, Map.of("offers", jobOffersUserAppliedFor), "Interested jobs fetched", org.springframework.http.HttpStatus.OK));
        } catch (Exception e) {
            log.error(e.getMessage());
            handleErrorResponse(request, response, e);
            return null;
        }
    }

    @GetMapping("/user/accepted")
    public ResponseEntity<Response> getAcceptedJobsForUser(HttpServletRequest request, HttpServletResponse response) {
        try{
            List<JobOfferResponse> jobOffersUserAccepted = applicationService.getJobOffersUserAccepted(RequestContext.getUserId());
            return ResponseEntity.ok().body(getResponse(request, Map.of("offers", jobOffersUserAccepted), "Accepted jobs fetched", org.springframework.http.HttpStatus.OK));
        } catch (Exception e) {
            log.error(e.getMessage());
            handleErrorResponse(request, response, e);
            return null;
        }
    }

    @GetMapping("/user/job-offer/{jobOfferId}")
    public ResponseEntity<Response> getApplicationForUserAndJob(HttpServletRequest request, @PathVariable("jobOfferId") String jobOfferId, HttpServletResponse response) {
        try{
            ApplicationResponse applicationResponse = applicationService.getApplicationForUserAndJob(RequestContext.getUserId(), jobOfferId);
            return ResponseEntity.ok().body(getResponse(request, Map.of("application", applicationResponse), "Interest fetched", org.springframework.http.HttpStatus.OK));
        } catch (Exception e) {
            log.error(e.getMessage());
            handleErrorResponse(request, response, e);
            return null;
        }
    }

    @PatchMapping("/{applicationId}")
    public ResponseEntity<Response> updateApplication(
            @PathVariable("applicationId") String applicationId,
            @RequestBody @Valid ApplicationResponseRequest applicationResponseRequest,
            HttpServletRequest request,
            HttpServletResponse response) {
        try{
            applicationService.updateApplication(applicationId, applicationResponseRequest);
            return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Interest updated", org.springframework.http.HttpStatus.OK));
        } catch (Exception e) {
            log.error(e.getMessage());
            handleErrorResponse(request, response, e);
            return null;
        }
    }

    @PatchMapping("/{applicationId}/acknowledge")
    public ResponseEntity<Response> acknowledgeAcceptance(@PathVariable("applicationId") String applicationId, HttpServletRequest request, HttpServletResponse response) {
        try{
            applicationService.acknowledgeAcceptance(applicationId);
            return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Interest acknowledged", org.springframework.http.HttpStatus.OK));
        } catch (Exception e) {
            log.error(e.getMessage());
            handleErrorResponse(request, response, e);
            return null;
        }
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<Response> deleteApplication(@PathVariable("applicationId") String id, HttpServletRequest request, HttpServletResponse response) {
        try{
            applicationService.deleteApplication(id);
            return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Interest deleted", org.springframework.http.HttpStatus.OK));
        } catch (Exception e) {
            log.error(e.getMessage());
            handleErrorResponse(request, response, e);
            return null;
        }
    }
}
