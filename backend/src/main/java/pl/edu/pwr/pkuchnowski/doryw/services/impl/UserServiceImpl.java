package pl.edu.pwr.pkuchnowski.doryw.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import pl.edu.pwr.pkuchnowski.doryw.cache.CacheStore;
import pl.edu.pwr.pkuchnowski.doryw.domain.RequestContext;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.User;
import pl.edu.pwr.pkuchnowski.doryw.entities.ConfirmationEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.RoleEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.UserEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.UserCredentialsEntity;
import pl.edu.pwr.pkuchnowski.doryw.enumeration.Authority;
import pl.edu.pwr.pkuchnowski.doryw.enumeration.EventType;
import pl.edu.pwr.pkuchnowski.doryw.enumeration.LoginType;
import pl.edu.pwr.pkuchnowski.doryw.events.UserEvent;
import pl.edu.pwr.pkuchnowski.doryw.exception.ApiException;
import pl.edu.pwr.pkuchnowski.doryw.repositories.ConfirmationRepository;
import pl.edu.pwr.pkuchnowski.doryw.repositories.RoleRepository;
import pl.edu.pwr.pkuchnowski.doryw.repositories.UserCredentialsRepository;
import pl.edu.pwr.pkuchnowski.doryw.repositories.UserRepository;
import pl.edu.pwr.pkuchnowski.doryw.services.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static pl.edu.pwr.pkuchnowski.doryw.utils.UserUtils.createUserEntity;
import static pl.edu.pwr.pkuchnowski.doryw.utils.UserUtils.fromUserEntity;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final ConfirmationRepository confirmationRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CacheStore<String, Integer> userLoginCache;
    private final ApplicationEventPublisher publisher;

    @Override
    public void createUser(String firstName, String lastName, String email, String password, String birthDate, String origin) {
        UserEntity userEntity = userRepository.save(createNewUser(firstName, lastName, email, birthDate));
        userEntity.setLastRefreshTokenInvalidation(new Date());
        String encodedPassword = passwordEncoder.encode(password);
        UserCredentialsEntity userCredentials = new UserCredentialsEntity(encodedPassword, userEntity);
        userCredentialsRepository.save(userCredentials);
        ConfirmationEntity confirmation = new ConfirmationEntity(userEntity);
        confirmationRepository.save(confirmation);
        publisher.publishEvent(new UserEvent(userEntity, EventType.REGISTRATION, Map.of("key", confirmation.getToken()), origin));
    }

    @Override
    public RoleEntity getRoleName(String name) {
        Optional<RoleEntity> role = roleRepository.findByNameIgnoreCase(name);
        return role.orElseThrow(() -> new IllegalArgumentException("Role not found"));
    }

    @Override
    public void verifyAccountToken(String token) {
        ConfirmationEntity confirmation = getConfirmation(token);
        UserEntity userEntity = getUserEntityById(confirmation.getUserId());
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        confirmationRepository.delete(confirmation);
    }

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        var userEntity = getUserEntityByEmail(email);
        RequestContext.setUser(userEntity);
        switch (loginType) {
            case LOGIN_ATTEMPT -> {
                if(userLoginCache.get(userEntity.getEmail()) == null) {
                    userEntity.setLoginAttempts(0);
                    userEntity.setAccountNonLocked(true);
                }
                userEntity.setLoginAttempts(userEntity.getLoginAttempts() + 1);
                userLoginCache.put(userEntity.getEmail(), userEntity.getLoginAttempts());
                if(userLoginCache.get(userEntity.getEmail()) > 5) {
                    userEntity.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                userEntity.setLoginAttempts(0);
                userEntity.setAccountNonLocked(true);
                userEntity.setLastLoginAttempt(LocalDateTime.now());
                userLoginCache.evict(userEntity.getEmail());
            }
        }
        userRepository.save(userEntity);
    }

    @Override
    public User getUserByUserReferenceId(String userReferenceId) {
       UserEntity userEntity = userRepository.findUserEntityByReferenceId(userReferenceId).orElseThrow(() -> new ApiException("User not found"));
        return fromUserEntity(userEntity);
    }

    @Override
    public User getUserByEmail(String email) {
        var userEntity = getUserEntityByEmail(email);
        return fromUserEntity(userEntity);
    }

    @Override
    public User getUserById(Long userId) {
        var userEntity = getUserEntityById(userId);
        return fromUserEntity(userEntity);
    }


    @Override
    public UserCredentialsEntity getUserCredentialById(Long userId) {
        var credentials = userCredentialsRepository.getUserCredentialsByUserEntityId(userId);
        return credentials.orElseThrow(() -> new ApiException("Credentials not found"));
    }

    @Override
    public UserCredentialsEntity getUserCredentialByReferenceId(String referenceId) {
        var credentials = userCredentialsRepository.getUserCredentialsByUserEntityReferenceId(referenceId);
        return credentials.orElseThrow(() -> new ApiException("Credentials not found"));
    }

    @Override
    public UserEntity updateUser(Long userId, String firstName, String lastName, String email, String phoneNumber, String birthDate) {
        try {
            var userEntity = getUserEntityById(userId);

            if (firstName != null) userEntity.setFirstName(firstName);
            if (lastName != null) userEntity.setLastName(lastName);
            if (email != null) userEntity.setEmail(email);
            if (phoneNumber != null) userEntity.setPhoneNumber(phoneNumber);
            if (birthDate != null) userEntity.setBirthDate(LocalDate.parse(birthDate));

            userRepository.save(userEntity);
            return userEntity;

        } catch (TransactionSystemException ex) {
            Throwable rootCause = ex.getRootCause();
            log.error("TransactionSystemException encountered: {}", ex.getMessage(), ex);
            if (rootCause != null) {
                log.error("Root cause: {}", rootCause.getMessage(), rootCause);
            }
            throw ex;
        } catch (Exception ex) {
            log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
            throw ex;
        }

    }

    @Override
    public Long getUserIdByUserReferenceId(String subject) {
        return userRepository.findUserEntityByReferenceId(subject)
                .orElseThrow(() -> new ApiException("User not found"))
                .getId();
    }

    @Override
    public UserEntity getUserEntityByUserReferenceId(String subject) {
        return userRepository.findUserEntityByReferenceId(subject)
                .orElseThrow(() -> new ApiException("User not found"));
    }

    @Override
    public UserEntity getUserEntityByEmail(String email) {
        Optional<UserEntity> userEntity = userRepository.findByEmailIgnoreCase(email);
        if(userEntity.isPresent()){
            return userEntity.get();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Override
    public UserEntity getUserEntityById(Long Id) {
        Optional<UserEntity> userEntity = userRepository.findUserEntityById(Id);
        if(userEntity.isPresent()){
            return userEntity.get();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    private ConfirmationEntity getConfirmation(String token) {
        Optional<ConfirmationEntity> confirmation = confirmationRepository.findByToken(token);
        if(confirmation.isPresent()){
            return confirmation.get();
        } else {
            throw new IllegalArgumentException("Confirmation not found");
        }
    }

    private UserEntity createNewUser(String firstName, String lastName, String email, String birthDate) {
        RoleEntity role = getRoleName(Authority.USER.name());
        return createUserEntity(firstName, lastName, email, birthDate, role);
    }
}
