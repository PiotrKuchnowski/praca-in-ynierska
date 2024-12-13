package pl.edu.pwr.pkuchnowski.doryw.constants;

import java.util.List;

public class Constants {
    public static final List<String> PATHS_WITHOUT_ROLES = List.of(
            "/user/register",
            "/user/verify-account",
            "/user/login",
            "/job-offers/{jobOfferId}",
            "/job-offers",
            "/error",
            "/employer/verify-account",
            "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**"
    );
    public static final String LOGIN_PATH = "/user/login";
    public static final String AUTHORITIES = "authorities";
    public static final String EMPTY_VALUE = "empty";
    public static final String ROLE = "role";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String AUTHORITY_DELIMITER = ",";
    public static final String USER_AUTHORITIES = "offer:read,employer:create,employee:create";
    public static final String EMPLOYEE_AUTHORITIES = "offer:read,interest:create,interest:delete,interest:read,interest:update";
    public static final String EMPLOYER_AUTHORITIES = "offer:create,offer:delete,offer:read,offer:update";
    public static final String ADMIN_AUTHORITIES = "offer:create,offer:delete,offer:read,offer:update,employer:create,employer:delete,employer:read,employer:update,employee:create,employee:delete,employee:read,employee:update";
    public static final String DORYW = "DORYW";
    public static final int NINETY_DAYS = 90;
    public static final int STRENGTH = 12;
    public static final List<String> CORS_ORIGINS = List.of(
            "http://localhost:3000",
            "http://192.168.196.4:3000",
            "http://192.168.196.*"
    );
    public static final List<String> ALL_LIST = List.of("*");
    public static final int ONE_WEEK_IN_MILLIS = 604800000;
}
