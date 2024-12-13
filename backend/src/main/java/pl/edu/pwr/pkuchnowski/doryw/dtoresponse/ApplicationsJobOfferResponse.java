package pl.edu.pwr.pkuchnowski.doryw.dtoresponse;

import lombok.Data;

import java.util.List;

@Data
public class ApplicationsJobOfferResponse {
    private JobOfferResponse jobOfferResponse;
    private List<ApplicationResponse> applications;
}
