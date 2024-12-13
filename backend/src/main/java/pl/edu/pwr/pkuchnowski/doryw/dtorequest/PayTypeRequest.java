package pl.edu.pwr.pkuchnowski.doryw.dtorequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayTypeRequest {
    @NotEmpty(message = "Name is required")
    private String name;
}
