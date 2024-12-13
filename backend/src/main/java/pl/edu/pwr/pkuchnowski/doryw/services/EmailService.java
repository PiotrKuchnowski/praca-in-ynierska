package pl.edu.pwr.pkuchnowski.doryw.services;

public interface EmailService {
    void sendNewAccountEmail(String name, String to, String token, String origin);
    void sendPasswordResetEmail(String name, String to, String token, String origin);
    void sendNewEmployerAccountEmail(String name, String to, String token, String origin);
    void sendEmployerDeletionEmail(String firstName, String email, String frontendURL);
}
