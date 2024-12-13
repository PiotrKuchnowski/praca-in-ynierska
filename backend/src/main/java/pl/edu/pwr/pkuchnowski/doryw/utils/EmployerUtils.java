package pl.edu.pwr.pkuchnowski.doryw.utils;

import lombok.RequiredArgsConstructor;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.EmployerResponse;
import pl.edu.pwr.pkuchnowski.doryw.entities.EmployerEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.UserCredentialsEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.UserEntity;
import pl.edu.pwr.pkuchnowski.doryw.repositories.UserCredentialsRepository;

import static pl.edu.pwr.pkuchnowski.doryw.utils.UserUtils.fromUserEntity;

@RequiredArgsConstructor
public class EmployerUtils {
    public static EmployerEntity createEmployerEntity(UserEntity userEntity, boolean isNaturalPerson, String companyName, String nip, String description) {
        EmployerEntity employerEntity = new EmployerEntity();
        employerEntity.setUserEntity(userEntity);
        employerEntity.setIsNaturalPerson(isNaturalPerson);
        if(isNaturalPerson) {
            employerEntity.setCompanyName(userEntity.getFirstName() + " " + userEntity.getLastName());
            if(nip != null) {
                employerEntity.setNip(nip);
            }
        }
        else {
            employerEntity.setCompanyName(companyName);
            employerEntity.setNip(nip);
        }
        employerEntity.setEnabled(false);
        employerEntity.setDescription(description);
        return employerEntity;
    }


    public static EmployerResponse fromEmployerEntity(EmployerEntity employerEntity) {
        EmployerResponse employer = new EmployerResponse();
        employer.setEmployerID(employerEntity.getId());
        employer.setDescription(employerEntity.getDescription());
        UserEntity userEntity = employerEntity.getUserEntity();
        employer.setUser(fromUserEntity(userEntity));
        return employer;
    }
}
