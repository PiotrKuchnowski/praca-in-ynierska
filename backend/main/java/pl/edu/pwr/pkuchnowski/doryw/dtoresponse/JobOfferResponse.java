package pl.edu.pwr.pkuchnowski.doryw.dtoresponse;

import lombok.Data;

import java.util.Set;

@Data
public class JobOfferResponse {
    private String jobOfferReferenceId;
    private EmployerResponse employerID;
    private String startDate;
    private String endDate;
    private String description;
    private String title;
    private Double pay;
    private PayTypeResponse payType;
    private Set<LocationResponse> locations;
    private String jobCategory;
}
