package pl.edu.pwr.pkuchnowski.doryw.events.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.edu.pwr.pkuchnowski.doryw.events.UserEvent;
import pl.edu.pwr.pkuchnowski.doryw.services.EmailService;

@Component
@RequiredArgsConstructor
public class UserEventListeners {
    private final EmailService emailService;

    @EventListener
    public void onUserEvent(UserEvent userEvent) {
        switch (userEvent.getType()) {
            case REGISTRATION:
                emailService.sendNewAccountEmail(
                        userEvent.getUserEntity().getFirstName(),
                        userEvent.getUserEntity().getEmail(),
                        userEvent.getData().get("key").toString(),
                        userEvent.getFrontendURL());
                break;
            case PASSWORD_RESET:
                emailService.sendPasswordResetEmail(
                        userEvent.getUserEntity().getFirstName(),
                        userEvent.getUserEntity().getEmail(),
                        userEvent.getData().get("key").toString(),
                        userEvent.getFrontendURL());
                break;
            default:
                break;
        }
    }
}
