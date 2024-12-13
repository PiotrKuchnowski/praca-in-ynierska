package pl.edu.pwr.pkuchnowski.doryw.dtorequest.patch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployerPatchRequest {
    private String companyName;
    private String nip;
    private String description;
}
