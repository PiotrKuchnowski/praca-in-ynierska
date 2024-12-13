package pl.edu.pwr.pkuchnowski.doryw.utils;

import org.springframework.beans.BeanUtils;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.User;
import pl.edu.pwr.pkuchnowski.doryw.entities.RoleEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.UserCredentialsEntity;
import pl.edu.pwr.pkuchnowski.doryw.entities.UserEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.edu.pwr.pkuchnowski.doryw.constants.Constants.NINETY_DAYS;

public class UserUtils {
    public static UserEntity createUserEntity(String firstName, String lastName, String email, String birthDate, RoleEntity role) {
        return UserEntity.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .roles(Set.of(role))
                .birthDate(LocalDate.parse(birthDate))
                .lastLoginAttempt(LocalDateTime.now())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(false)
                .loginAttempts(0)
                .build();
    }

    public static User fromUserEntity(UserEntity userEntity) {
        var user = new User();
        var roles = userEntity.getRoles();
        BeanUtils.copyProperties(userEntity, user);
        if (userEntity.getLoginAttempts() != null) {
            user.setLastLogin(userEntity.getLastLoginAttempt().toString());
        }
        user.setCreatedAt(userEntity.getCreatedAt().toString());
        user.setUpdatedAt(userEntity.getUpdatedAt().toString());
        user.setRoles(roles.stream().map(RoleEntity::getName).collect(Collectors.toSet()));
        if (userEntity.getLastRefreshTokenInvalidation() != null) {
            user.setLastRefreshTokenInvalidation(userEntity.getLastRefreshTokenInvalidation().toString());
        }
        user.setCreatedBy(userEntity.getCreatedBy().getId());
        user.setUpdatedBy(userEntity.getUpdatedBy().getId());
        return user;
    }


    public static boolean isCredentialsNonExpired(UserCredentialsEntity userCredentialById) {
        return userCredentialById.getUpdatedAt().plusDays(NINETY_DAYS).isAfter(LocalDateTime.now());
    }
}
