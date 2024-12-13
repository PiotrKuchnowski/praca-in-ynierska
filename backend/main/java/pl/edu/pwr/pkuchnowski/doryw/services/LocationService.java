package pl.edu.pwr.pkuchnowski.doryw.services;

import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.CityResponse;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.LocationResponse;

import java.util.List;
import java.util.Set;

public interface LocationService {
    Set<CityResponse> getLocations();
}
