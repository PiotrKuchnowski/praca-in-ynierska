package pl.edu.pwr.pkuchnowski.doryw.enumeration;

import lombok.Getter;

import static pl.edu.pwr.pkuchnowski.doryw.constants.Constants.*;

@Getter
public enum Authority {
    USER(USER_AUTHORITIES),
    EMPLOYEE(EMPLOYEE_AUTHORITIES),
    EMPLOYER(EMPLOYER_AUTHORITIES),
    ADMIN(ADMIN_AUTHORITIES);

    private final String value;

    Authority(String value) {
        this.value = value;
    }

    public static Authority fromDbData(String dbData) {
        return switch (dbData) {
            case USER_AUTHORITIES -> USER;
            case EMPLOYEE_AUTHORITIES -> EMPLOYEE;
            case EMPLOYER_AUTHORITIES -> EMPLOYER;
            case ADMIN_AUTHORITIES -> ADMIN;
            default -> throw new IllegalArgumentException("Unknown dbData value: " + dbData);
        };
    }
}
