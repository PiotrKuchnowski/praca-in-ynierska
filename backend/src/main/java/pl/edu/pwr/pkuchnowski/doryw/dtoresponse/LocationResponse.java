package pl.edu.pwr.pkuchnowski.doryw.dtoresponse;

import lombok.Data;

@Data
public class LocationResponse {
    private String locationReferenceID;
    private boolean remote;
    private String postalCode;
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private String apartmentNumber;
}
