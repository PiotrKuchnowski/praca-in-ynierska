package pl.edu.pwr.pkuchnowski.doryw.dtorequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import pl.edu.pwr.pkuchnowski.doryw.validation.location.ValidLocationRequest;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ValidLocationRequest
public class LocationRequest {
    private boolean remote;
    private String postalCode;
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private String apartmentNumber;
}
