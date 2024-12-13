package pl.edu.pwr.pkuchnowski.doryw.dtorequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobOfferRequest {

    @NotEmpty(message = "Start date is required")
    private String startDate;

    private String endDate;

    @NotEmpty(message = "Description is required")
    private String description;

    @NotEmpty(message = "Title is required")
    private String title;

    @NotNull(message = "Pay is required")
    private Double pay;

    @NotNull(message = "Pay type is required")
    private PayTypeRequest payType;

    @NotEmpty(message = "Minimum one location is required")
    private List<LocationRequest> locations;

    @NotEmpty(message = "Job category is required")
    private String jobCategory;
}
