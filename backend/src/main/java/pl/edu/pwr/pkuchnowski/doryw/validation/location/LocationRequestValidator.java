package pl.edu.pwr.pkuchnowski.doryw.validation.location;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.edu.pwr.pkuchnowski.doryw.dtorequest.LocationRequest;

public class LocationRequestValidator implements ConstraintValidator<ValidLocationRequest, LocationRequest> {

    @Override
    public void initialize(ValidLocationRequest constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocationRequest locationRequest, ConstraintValidatorContext context) {
        if (!locationRequest.isRemote()) {
            return locationRequest.getPostalCode() != null &&
                    locationRequest.getCity() != null &&
                    locationRequest.getHouseNumber() != null
                    && locationRequest.getCountry() != null;
        }
        return true;
    }
}
