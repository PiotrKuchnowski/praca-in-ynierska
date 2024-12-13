package pl.edu.pwr.pkuchnowski.doryw.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
