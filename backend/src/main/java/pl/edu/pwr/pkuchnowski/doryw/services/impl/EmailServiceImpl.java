package pl.edu.pwr.pkuchnowski.doryw.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import pl.edu.pwr.pkuchnowski.doryw.services.EmailService;

import static pl.edu.pwr.pkuchnowski.doryw.utils.EmailUtils.*;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private static final String NEW_USER_ACCOUNT_VERIFICATION = "Potwierdzenie nowego konta";
    private static final String PASSWORD_RESET_REQUEST = "Prośba o zresetowanie hasła";
    private static final String NEW_EMPLOYER_ACCOUNT_VERIFICATION = "Potwierdzenie nowego konta pracodawcy";
    private final JavaMailSender sender;
    @Value("${spring.mail.properties.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async
    public void sendNewAccountEmail(String name, String email, String token, String origin) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setText(getNewAccountEmailMessage(name, token, origin));
            sender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @Async
    public void sendPasswordResetEmail(String name, String email, String token, String origin) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(PASSWORD_RESET_REQUEST);
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setText(getResetPasswordMessage(name, token, origin));
            sender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @Async
    public void sendNewEmployerAccountEmail(String name, String to, String token, String frontendURL) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_EMPLOYER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getNewEmployerAccountEmailMessage(name, token, frontendURL));
            sender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @Override
    public void sendEmployerDeletionEmail(String firstName, String email, String frontendURL) {

    }

}
