package pl.edu.pwr.pkuchnowski.doryw;


import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import pl.edu.pwr.pkuchnowski.doryw.domain.RequestContext;
import pl.edu.pwr.pkuchnowski.doryw.entities.RoleEntity;
import pl.edu.pwr.pkuchnowski.doryw.enumeration.Authority;
import pl.edu.pwr.pkuchnowski.doryw.repositories.EmployerRepository;
import pl.edu.pwr.pkuchnowski.doryw.repositories.RoleRepository;
import pl.edu.pwr.pkuchnowski.doryw.repositories.UserRepository;

import javax.sql.DataSource;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class DorywApplication {

    public static void main(String[] args) {
        SpringApplication.run(DorywApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(RoleRepository roleRepository, UserRepository userRepository,
                                        EmployerRepository employerRepository, DataSource dataSource) {
        return args -> {
            JdbcTemplate jdbc = new JdbcTemplate(dataSource);
            if(!userRepository.existsById(0L)) {
                jdbc.execute("INSERT INTO users (id, reference_id, email_address, first_name, last_name, birth_date," +
                        " created_at, created_by, updated_at, updated_by, account_non_expired, account_non_locked, enabled," +
                        "last_login_attempt) " +
                        "VALUES (0, gen_random_uuid() ,'system@example.com', 'System', 'System', now(), now(), 0, now(), 0," +
                        "true, true, true, now())");
                jdbc.execute("INSERT INTO user_credentials (id, reference_id, user_id, password, created_at, created_by, updated_at, updated_by) " +
                        "VALUES (0, gen_random_uuid(), 0, '$2a$10$3Zz9Zz9Zz9Zz9Zz9Zz9ZzO', now(), 0, now(), 0)");
            }
            if(!employerRepository.existsByUserEntityId(0L)) {
                jdbc.execute("INSERT INTO employers (id, reference_id, user_id, company_name, created_at, created_by, updated_at, updated_by, is_natural_person, description, enabled) " +
                        "VALUES (0, gen_random_uuid(), 0, 'System', now(), 0, now(), 0, false, 'System', false)");
                System.out.println("User created");
            }

            if (!roleRepository.existsByNameIgnoreCase(Authority.USER.name())) {
                RoleEntity userRole = new RoleEntity();
                userRole.setName(Authority.USER.name());
                roleRepository.save(userRole);
            }

            if (!roleRepository.existsByNameIgnoreCase(Authority.EMPLOYER.name())) {
                RoleEntity employerRole = new RoleEntity();
                employerRole.setName(Authority.EMPLOYER.name());
                roleRepository.save(employerRole);
            }

            if (!roleRepository.existsByNameIgnoreCase(Authority.ADMIN.name())) {
                RoleEntity adminRole = new RoleEntity();
                adminRole.setName(Authority.ADMIN.name());
                roleRepository.save(adminRole);
            }
        };
    }

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
