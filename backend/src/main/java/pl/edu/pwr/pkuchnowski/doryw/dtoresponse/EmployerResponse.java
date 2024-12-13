package pl.edu.pwr.pkuchnowski.doryw.dtoresponse;

import lombok.Data;

@Data
public class EmployerResponse {
    private Long employerID;
    private String description;
    private User user;
}
