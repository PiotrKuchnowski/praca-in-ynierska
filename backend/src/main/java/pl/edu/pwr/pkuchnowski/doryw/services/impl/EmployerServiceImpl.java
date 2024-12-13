package pl.edu.pwr.pkuchnowski.doryw.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.edu.pwr.pkuchnowski.doryw.domain.RequestContext;
import pl.edu.pwr.pkuchnowski.doryw.dtorequest.EmployerRequest;
import pl.edu.pwr.pkuchnowski.doryw.dtorequest.patch.EmployerPatchRequest;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.EmployerProfileResponse;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.EmployerResponse;
import pl.edu.pwr.pkuchnowski.doryw.entities.ConfirmationEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.EmployerEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.UserEntity;
import pl.edu.pwr.pkuchnowski.doryw.enumeration.EventType;
import pl.edu.pwr.pkuchnowski.doryw.events.EmployerEvent;
import pl.edu.pwr.pkuchnowski.doryw.repositories.ConfirmationRepository;
import pl.edu.pwr.pkuchnowski.doryw.repositories.EmployerRepository;
import pl.edu.pwr.pkuchnowski.doryw.services.EmployerService;
import pl.edu.pwr.pkuchnowski.doryw.services.UserService;

import java.util.Map;
import java.util.Optional;

import static pl.edu.pwr.pkuchnowski.doryw.utils.EmployerUtils.*;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;
    private final UserService userService;
    private final ConfirmationRepository confirmationRepository;
    private final ApplicationEventPublisher publisher;

    @Override
    public void createNewEmployer(Long userId, Boolean isNaturalPerson, String companyName, String nip, String description, String origin) {
        UserEntity userEntity = userService.getUserEntityById(userId);
        EmployerEntity employerEntity = employerRepository.save(createEmployerEntity(userEntity, isNaturalPerson, companyName, nip, description));
        ConfirmationEntity confirmation = new ConfirmationEntity(userEntity);
        confirmationRepository.save(confirmation);
        publisher.publishEvent(new EmployerEvent(employerEntity, EventType.REGISTRATION, Map.of("key", confirmation.getToken()), origin));
    }

    @Override
    public void verifyAccountToken(String token) {
        ConfirmationEntity confirmation = getConfirmation(token);
        EmployerEntity employerEntity = getEmployerEntityByUserEntity(confirmation.getCreatedBy());
        employerEntity.setEnabled(true);
        employerRepository.save(employerEntity);
        confirmationRepository.delete(confirmation);
    }

    @Override
    public Boolean isEmployer(String userReferenceId) {
        Optional<EmployerEntity> employerEntity = employerRepository.findByUserEntityReferenceId(userReferenceId);
        return employerEntity.isPresent();
    }

    @Override
    public Optional<EmployerEntity> getEmployerEntityByUserReferenceId(String userReferenceId) {
        return employerRepository.findByUserEntityReferenceId(userReferenceId);
    }

    @Override
    public Long getEmployerIdByUserId(Long userId) {
        Optional<EmployerEntity> employerEntity = employerRepository.findByUserEntityId(userId);
        if(employerEntity.isPresent()){
            return employerEntity.get().getId();
        } else {
            throw new IllegalArgumentException("Employer not found");
        }
    }

    @Override
    public void updateEmployer(EmployerPatchRequest employer) {
        Optional<EmployerEntity> employerOptional = getEmployerEntityByUserId(RequestContext.getUserId());
        if(employerOptional.isEmpty()){
            throw new IllegalArgumentException("Employer not found");
        }
        EmployerEntity employerEntity = employerOptional.get();
        employerEntity.setCompanyName(employer.getCompanyName());
        employerEntity.setNip(employer.getNip());
        employerEntity.setDescription(employer.getDescription());
        employerRepository.save(employerEntity);
    }

    @Override
    public Optional<EmployerEntity> getEmployerEntityByUserId(Long userId) {
        return employerRepository.findByUserEntityId(userId);
    }

    @Override
    public void deleteEmployer(Long userId) {
        Optional<EmployerEntity> employerEntity = employerRepository.findByUserEntityId(userId);
        if(employerEntity.isPresent()){
            employerRepository.delete(employerEntity.get());
        } else {
            throw new IllegalArgumentException("Employer not found");
        }
    }

    @Override
    public EmployerProfileResponse getEmployer(Long userId) {
        Optional<EmployerEntity> employerEntity = employerRepository.findByUserEntityId(userId);
        if(employerEntity.isPresent()){
            return createEmployerResponse(employerEntity.get());
        } else {
            throw new IllegalArgumentException("Employer not found");
        }
    }

    private EmployerProfileResponse createEmployerResponse(EmployerEntity employerEntity) {
        return new EmployerProfileResponse(employerEntity.getIsNaturalPerson(), employerEntity.getCompanyName(), employerEntity.getNip(), employerEntity.getDescription());
    }

    private ConfirmationEntity getConfirmation(String token) {
        Optional<ConfirmationEntity> confirmation = confirmationRepository.findByToken(token);
        if(confirmation.isPresent()){
            return confirmation.get();
        } else {
            throw new IllegalArgumentException("Confirmation not found");
        }
    }

    private EmployerEntity getEmployerEntityByUserEntity(UserEntity userEntity) {
        Optional<EmployerEntity> employerEntity = employerRepository.findByUserEntity(userEntity);
        if(employerEntity.isPresent()){
            return employerEntity.get();
        } else {
            throw new IllegalArgumentException("Employer not found");
        }
    }
}
