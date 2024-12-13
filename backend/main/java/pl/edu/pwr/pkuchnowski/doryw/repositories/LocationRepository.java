package pl.edu.pwr.pkuchnowski.doryw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.pwr.pkuchnowski.doryw.entities.LocationEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    Optional<LocationEntity> findByCountryAndCityAndPostalCodeAndStreetAndHouseNumberAndApartmentNumber(
            String country,
            String city,
            String postalCode,
            String street,
            String houseNumber,
            String apartmentNumber
    );

    @Query("SELECT l FROM LocationEntity l WHERE l.remote = true")
    Optional<LocationEntity> findRemoteLocation();

    List<LocationEntity> findAll();
}
