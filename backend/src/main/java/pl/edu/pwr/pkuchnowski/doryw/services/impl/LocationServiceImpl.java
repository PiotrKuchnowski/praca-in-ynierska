package pl.edu.pwr.pkuchnowski.doryw.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.CityResponse;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.LocationResponse;
import pl.edu.pwr.pkuchnowski.doryw.entities.LocationEntity;
import pl.edu.pwr.pkuchnowski.doryw.repositories.LocationRepository;
import pl.edu.pwr.pkuchnowski.doryw.services.LocationService;
import pl.edu.pwr.pkuchnowski.doryw.utils.LocationUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static pl.edu.pwr.pkuchnowski.doryw.utils.LocationUtils.*;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public Set<CityResponse> getLocations() {
        List<LocationEntity> locationEntities = locationRepository.findAll();
        Set<LocationEntity> locationEntitiesSet = Set.copyOf(locationEntities);
        Set<LocationResponse> locationResponses = fromLocationEntities(locationEntitiesSet);
        return locationResponses.stream()
                .map(locationResponse -> new CityResponse(locationResponse.getCity()))
                .collect(Collectors.toSet());
    }


}
