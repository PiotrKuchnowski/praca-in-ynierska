package pl.edu.pwr.pkuchnowski.doryw.utils;

import pl.edu.pwr.pkuchnowski.doryw.dtorequest.JobOfferRequest;
import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.LocationResponse;
import pl.edu.pwr.pkuchnowski.doryw.entities.LocationEntity;
import pl.edu.pwr.pkuchnowski.doryw.repositories.LocationRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class LocationUtils {

    public static Set<LocationResponse> fromLocationEntities(Set<LocationEntity> locationEntity) {
        Set<LocationResponse> locations = new HashSet<>();
        for(LocationEntity entity : locationEntity) {
            LocationResponse location = new LocationResponse();
            location.setLocationReferenceID(entity.getReferenceId());
            location.setRemote(entity.isRemote());
            location.setCountry(entity.getCountry());
            location.setPostalCode(entity.getPostalCode());
            location.setCity(entity.getCity());
            location.setStreet(entity.getStreet());
            location.setHouseNumber(entity.getHouseNumber());
            location.setApartmentNumber(entity.getApartmentNumber());
            locations.add(location);
        }
        return locations;
    }

    public static Set<LocationEntity> addNonExistingLocations(JobOfferRequest jobOffer, LocationRepository locationRepository) {
        return jobOffer.getLocations().stream().map(location -> {
            LocationEntity locationEntity;
            if(location.isRemote()) {
                locationEntity = locationRepository.findRemoteLocation().orElse(null);
            }
            else{
                locationEntity = locationRepository.findByCountryAndCityAndPostalCodeAndStreetAndHouseNumberAndApartmentNumber(
                        location.getCountry(),
                        location.getCity(),
                        location.getPostalCode(),
                        location.getStreet(),
                        location.getHouseNumber(),
                        location.getApartmentNumber()
                ).orElse(null);
            }

            if (locationEntity == null) {
                locationEntity = LocationEntity.builder()
                        .country(location.getCountry())
                        .city(location.getCity())
                        .postalCode(location.getPostalCode())
                        .street(location.getStreet())
                        .houseNumber(location.getHouseNumber())
                        .apartmentNumber(location.getApartmentNumber())
                        .remote(location.isRemote())
                        .build();
            }
            locationRepository.save(locationEntity);
            return locationEntity;
        }).collect(Collectors.toSet());
    }

}
