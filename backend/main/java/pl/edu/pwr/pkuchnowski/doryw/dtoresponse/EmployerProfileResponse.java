package pl.edu.pwr.pkuchnowski.doryw.dtoresponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployerProfileResponse {
    private Boolean isNaturalPerson;
    private String companyName;
    private String nip;
    private String description;
}
