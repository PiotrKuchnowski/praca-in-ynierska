package pl.edu.pwr.pkuchnowski.doryw.dtoresponse;

import lombok.Data;

@Data
public class ApplicationResponse {
    String applicationId;
    String message;
    String response;
    String status;
    UserProfileResponse user;
}
