package pl.edu.pwr.pkuchnowski.doryw.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pwr.pkuchnowski.doryw.domain.RequestContext;
import pl.edu.pwr.pkuchnowski.doryw.domain.Response;
import pl.edu.pwr.pkuchnowski.doryw.dtorequest.EmployerRequest;
import pl.edu.pwr.pkuchnowski.doryw.dtorequest.patch.EmployerPatchRequest;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.EmployerProfileResponse;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.EmployerResponse;
import pl.edu.pwr.pkuchnowski.doryw.services.EmployerService;

import java.net.URI;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static pl.edu.pwr.pkuchnowski.doryw.utils.RequestUtils.getResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employer")
public class EmployerController {
    private final EmployerService employerService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerEmployer(@RequestBody @Valid EmployerRequest employer, HttpServletRequest request) {
        String verificationUrl = request.getHeader("origin")  + employer.getVerificationUrl();
        employerService.createNewEmployer(RequestContext.getUserId(), employer.getIsNaturalPerson(), employer.getCompanyName(), employer.getNip(), employer.getDescription(), verificationUrl);
        return ResponseEntity.created(getUri()).body(getResponse(request, emptyMap(), "Konto zostało utworzone. Sprawdź swoją skrzynkę mailową, aby aktywować konto.", HttpStatus.CREATED));
    }

    @PostMapping("/verify-account")
    public ResponseEntity<Response> verifyEmployerAccount(@RequestParam("token") String token, HttpServletRequest request) {
        employerService.verifyAccountToken(token);
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Konto zostało zweryfikowane!.", HttpStatus.OK));
    }

    @GetMapping("")
    public ResponseEntity<Response> getEmployer(HttpServletRequest request) {
        EmployerProfileResponse employer = employerService.getEmployer(RequestContext.getUserId());
        return ResponseEntity.ok().body(getResponse(request, Map.of("employer", employer), "Dane pracodawcy zostały pobrane.", HttpStatus.OK));
    }

    @PatchMapping("")
    public ResponseEntity<Response> updateEmployer(@RequestBody @Valid EmployerPatchRequest employer, HttpServletRequest request) {
        employerService.updateEmployer(employer);
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Konto zostało zaktualizowane.", HttpStatus.OK));
    }

    @DeleteMapping("")
    public ResponseEntity<Response> deleteEmployer( HttpServletRequest request) {
        employerService.deleteEmployer(RequestContext.getUserId());
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Konto zostało usunięte.", HttpStatus.OK));
    }

    private URI getUri() {
        return URI.create("");
    }
}
