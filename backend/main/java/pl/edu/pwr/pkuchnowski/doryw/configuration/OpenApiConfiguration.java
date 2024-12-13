package pl.edu.pwr.pkuchnowski.doryw.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Doryw API for posting job offers",
                version = "1.0",
                description = "API for posting job offers",
                contact = @Contact(
                        name = "Piotr Kuchnowski",
                        email = "kuchnowski.piotr@outlook.com"
                )
        )
)
public class OpenApiConfiguration {}