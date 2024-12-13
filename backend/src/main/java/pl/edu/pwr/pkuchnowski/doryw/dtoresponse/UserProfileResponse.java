package pl.edu.pwr.pkuchnowski.doryw.dtoresponse;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileResponse {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthDate;
}
