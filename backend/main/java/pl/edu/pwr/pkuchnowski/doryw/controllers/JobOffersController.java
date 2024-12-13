package pl.edu.pwr.pkuchnowski.doryw.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pwr.pkuchnowski.doryw.domain.RequestContext;
import pl.edu.pwr.pkuchnowski.doryw.domain.Response;
import pl.edu.pwr.pkuchnowski.doryw.dtorequest.JobOfferRequest;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.CityResponse;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.JobCategoryResponse;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.JobOfferResponse;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.LocationResponse;
import pl.edu.pwr.pkuchnowski.doryw.services.EmployerService;
import pl.edu.pwr.pkuchnowski.doryw.services.JobCategoryService;
import pl.edu.pwr.pkuchnowski.doryw.services.JobOfferService;
import pl.edu.pwr.pkuchnowski.doryw.services.LocationService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.*;
import static org.springframework.http.HttpStatus.OK;
import static pl.edu.pwr.pkuchnowski.doryw.utils.JobOfferUtils.fromJobCategoryEntities;
import static pl.edu.pwr.pkuchnowski.doryw.utils.RequestUtils.getResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-offers")
public class JobOffersController {

    private final JobOfferService jobOfferService;
    private final EmployerService employerService;
    private final JobCategoryService jobCategoryService;
    private final LocationService locationService;

    @PostMapping("")
    public ResponseEntity<Response> createOffer(HttpServletRequest request, @RequestBody @Valid JobOfferRequest jobOfferRequest) {
        jobOfferService.createJobOffer(jobOfferRequest);
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Job offer added", OK));
    }

    @GetMapping("")
    public ResponseEntity<Response> getAllOffers(HttpServletRequest request) {
        List<JobOfferResponse> jobOffers = jobOfferService.getAllJobOffers();
        return ResponseEntity.ok().body(getResponse(request, Map.of("offers", jobOffers), "All job offers", OK));
    }

    @GetMapping("/employer")
    public ResponseEntity<Response> getEmployerOffers(HttpServletRequest request) {
        List<JobOfferResponse> jobOffers = jobOfferService.getAllJobOffersForEmployer(employerService.getEmployerIdByUserId(RequestContext.getUser().getId()));
        return ResponseEntity.ok().body(getResponse(request, Map.of("offers", jobOffers), "All job offers for employer", OK));
    }

    @GetMapping("/{jobOfferId}")
    public ResponseEntity<Response> getOfferById(HttpServletRequest request, @PathVariable("jobOfferId") String jobOfferReferenceId) {
        JobOfferResponse jobOffer = jobOfferService.getJobOfferResponseByReferenceId(jobOfferReferenceId);
        return ResponseEntity.ok().body(getResponse(request, Map.of("offer", jobOffer), "Job offer with id: " + jobOfferReferenceId, OK));
    }

    @GetMapping("/categories")
    public ResponseEntity<Response> getAllCategories(HttpServletRequest request) {
        List<JobCategoryResponse> jobCategories = fromJobCategoryEntities(jobCategoryService.getAllJobCategories());
        return ResponseEntity.ok().body(getResponse(request, Map.of("categories", jobCategories), "All job categories", OK));
    }

    @GetMapping("/cities")
    public ResponseEntity<Response> getAllLocations(HttpServletRequest request) {
        Set<CityResponse> cities = locationService.getLocations();
        return ResponseEntity.ok().body(getResponse(request, Map.of("cities", cities), "All locations", OK));
    }

    @PatchMapping("/{jobOfferId}")
    public ResponseEntity<Response> updateOffer(
            HttpServletRequest request,
            @RequestBody @Valid JobOfferRequest jobOfferRequest,
            @PathVariable("jobOfferId") String jobOfferReferenceId) {
        jobOfferService.updateJobOffer(jobOfferReferenceId, jobOfferRequest);
        return ResponseEntity.ok().body(getResponse(request, Map.of(), "Job offer updated", OK));
    }

    @DeleteMapping("/{jobOfferId}")
    public ResponseEntity<Response> deleteOffer(HttpServletRequest request, @PathVariable("jobOfferId") String jobOfferReferenceId) {
        jobOfferService.deleteJobOffer(jobOfferReferenceId);
        return ResponseEntity.ok().body(getResponse(request, Map.of(), "Job offer deleted", OK));
    }

}
