package pl.edu.pwr.pkuchnowski.doryw.services;

import pl.edu.pwr.pkuchnowski.doryw.dtorequest.EmployerRequest;
import pl.edu.pwr.pkuchnowski.doryw.dtorequest.patch.EmployerPatchRequest;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.EmployerProfileResponse;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.EmployerResponse;
import pl.edu.pwr.pkuchnowski.doryw.entities.EmployerEntity;

import java.util.Optional;

public interface EmployerService {
    void createNewEmployer(Long userId, Boolean isNaturalPerson, String companyName, String nip, String description, String domain);
    void verifyAccountToken(String token);
    Boolean isEmployer(String userReferenceId);
    Optional<EmployerEntity> getEmployerEntityByUserReferenceId(String userReferenceId);
    Long getEmployerIdByUserId(Long userId);
    void updateEmployer(EmployerPatchRequest employer);
    Optional<EmployerEntity> getEmployerEntityByUserId(Long userId);

    void deleteEmployer(Long userId);

    EmployerProfileResponse getEmployer(Long userId);
}
