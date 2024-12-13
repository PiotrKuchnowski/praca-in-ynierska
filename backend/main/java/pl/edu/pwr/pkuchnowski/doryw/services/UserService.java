package pl.edu.pwr.pkuchnowski.doryw.services;

import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.User;
import pl.edu.pwr.pkuchnowski.doryw.entities.RoleEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.UserCredentialsEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.UserEntity;
import pl.edu.pwr.pkuchnowski.doryw.enumeration.LoginType;

public interface UserService {
    void createUser(String firstName, String lastName, String email, String password, String birthDate, String origin);
    RoleEntity getRoleName(String name);
    void verifyAccountToken(String token);
    void updateLoginAttempt(String email, LoginType loginType);
    User getUserByUserReferenceId(String userId);
    User getUserByEmail(String email);
    User getUserById(Long userId);
    UserCredentialsEntity getUserCredentialById(Long id);
    UserCredentialsEntity getUserCredentialByReferenceId(String referenceId);
    UserEntity updateUser(Long userId, String firstName, String lastName, String email, String phoneNumber, String birthDate);
    Long getUserIdByUserReferenceId(String subject);
    UserEntity getUserEntityByUserReferenceId(String subject);
    UserEntity getUserEntityByEmail(String email);
    UserEntity getUserEntityById(Long userId);
    void deleteUser(Long userId);
}
