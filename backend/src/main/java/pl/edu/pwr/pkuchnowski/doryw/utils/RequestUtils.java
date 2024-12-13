package pl.edu.pwr.pkuchnowski.doryw.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.edu.pwr.pkuchnowski.doryw.domain.Response;
import pl.edu.pwr.pkuchnowski.doryw.exception.ApiException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class RequestUtils {

    private static final BiConsumer<HttpServletResponse, Response> writeResponse = ((httpServletResponse, response) -> {
        try {
            var outputStream = httpServletResponse.getOutputStream();
            new ObjectMapper().writeValue(outputStream, response);
            outputStream.flush();
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    });

    private static final BiFunction<Exception, HttpStatus, String> errorReason = (e, status) -> {
        if (status.isSameCodeAs(FORBIDDEN)) {
            return "Access denied";
        }
        if (status.isSameCodeAs(UNAUTHORIZED)) {
            return "Unauthorized";
        }
        if (e instanceof DisabledException || e instanceof LockedException || e instanceof BadCredentialsException || e instanceof CredentialsExpiredException || e instanceof ApiException) {
            return e.getMessage();
        }
        if (e instanceof IllegalArgumentException) {
            return e.getMessage();
        }
        if (status.is5xxServerError()) {
            return "Internal server error occurred";
        } else {
            return "An error occurred. Please try again";
        }
    };

    public static Response getResponse(HttpServletRequest request, Map<?, ?> data, String message, HttpStatus status) {
        return new Response(LocalDateTime.now().toString(), status.value(), request.getRequestURI(), valueOf(status.value()), message, EMPTY, data);
    }

    public static void handleErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception e) {
        if (e instanceof AccessDeniedException) {
            var response1 = getErrorResponse(request, response, e, FORBIDDEN);
            writeResponse.accept(response, response1);
        }
        if (
                e instanceof DisabledException ||
                        e instanceof LockedException ||
                        e instanceof BadCredentialsException ||
                        e instanceof CredentialsExpiredException ||
                        e instanceof UsernameNotFoundException ||
                        e instanceof ExpiredJwtException
        ) {
            var response1 = getErrorResponse(request, response, e, UNAUTHORIZED);
            writeResponse.accept(response, response1);
        }
        if (e instanceof IllegalArgumentException) {
            var response1 = getErrorResponse(request, response, e, BAD_REQUEST);
            writeResponse.accept(response, response1);
        }
    }

    private static Response getErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception e, HttpStatus status) {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        return new Response(LocalDateTime.now().toString(), status.value(), request.getRequestURI(), valueOf(status.value()), errorReason.apply(e, status), e.getMessage(), emptyMap());
    }
}
