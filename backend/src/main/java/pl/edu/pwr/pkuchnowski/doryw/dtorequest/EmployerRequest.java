package pl.edu.pwr.pkuchnowski.doryw.dtorequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployerRequest {
    @NotNull(message = "isNaturalPerson is required")
    private Boolean isNaturalPerson;
    private String companyName;
    private String nip;
    private String description;
    @NotEmpty(message = "verificationUrl is required")
    private String verificationUrl;
}
