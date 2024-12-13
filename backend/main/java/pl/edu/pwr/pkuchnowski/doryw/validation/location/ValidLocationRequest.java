package pl.edu.pwr.pkuchnowski.doryw.validation.location;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = LocationRequestValidator.class)
@Target({ElementType.TYPE})
public @interface ValidLocationRequest {
    String message() default "Invalid location request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
