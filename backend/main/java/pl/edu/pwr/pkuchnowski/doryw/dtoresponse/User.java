package pl.edu.pwr.pkuchnowski.doryw.dtoresponse;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;


@Data
public class User {
    private String referenceId;
    private Long createdBy;
    private Long updatedBy;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String lastLogin;
    private String lastRefreshTokenInvalidation;
    private String createdAt;
    private String updatedAt;
    private Set<String> roles;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean enabled;
    private boolean isCredentialsNonExpired;
    private Boolean isEmployer;
    private LocalDate birthDate;
}
