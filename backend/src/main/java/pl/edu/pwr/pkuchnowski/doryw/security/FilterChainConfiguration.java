package pl.edu.pwr.pkuchnowski.doryw.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import pl.edu.pwr.pkuchnowski.doryw.services.EmployerService;
import pl.edu.pwr.pkuchnowski.doryw.services.InvalidatedTokenService;
import pl.edu.pwr.pkuchnowski.doryw.services.JwtService;
import pl.edu.pwr.pkuchnowski.doryw.services.UserService;

import static pl.edu.pwr.pkuchnowski.doryw.constants.Constants.*;
import static pl.edu.pwr.pkuchnowski.doryw.constants.Constants.CORS_ORIGINS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class FilterChainConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationFilter authenticationFilter) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(CORS_ORIGINS);
                    corsConfiguration.setAllowedMethods(ALL_LIST);
                    corsConfiguration.setAllowedHeaders(ALL_LIST);
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/user/login").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/user/register").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/employer/register").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/employer/verify-account").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/employer").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/employer").hasRole("USER")

                        .requestMatchers(HttpMethod.POST, "/applications").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/applications/employer").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/applications/employer/accepted").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/applications/job-offer/{jobOfferId}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/applications/job-offer/{jobOfferId}/accepted").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/applications/user").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/applications/user/accepted").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/applications/{applicationId}").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/application/{applicationId}/acknowledge").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/application/{applicationId}").hasRole("USER")


                        .requestMatchers(HttpMethod.POST, "/job-offers").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/job-offers").permitAll()
                        .requestMatchers(HttpMethod.GET, "/job-offers/{jobOfferId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/job-offers/categories").permitAll()
                        .requestMatchers(HttpMethod.GET, "/job-offers/employer").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/job-offers/{jobOfferId}").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE,"/job-offers/{jobOfferId}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/job-offers/cities").permitAll()

                        .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/logout").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/user/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/verify-account").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/user").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/user").hasRole("USER")
                        .anyRequest().authenticated())
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserService userService, BCryptPasswordEncoder encoder) {
        ApiAuthenticationProvider provider = new ApiAuthenticationProvider(userService, encoder);
        return new ProviderManager(provider);
    }

    @Bean
    public AuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager, UserService userService, EmployerService employerService, JwtService jwtService, ObjectMapper objectMapper, InvalidatedTokenService invalidatedTokenService) {
        return new AuthenticationFilter(authenticationManager, userService, employerService, jwtService, objectMapper, invalidatedTokenService);
    }
}