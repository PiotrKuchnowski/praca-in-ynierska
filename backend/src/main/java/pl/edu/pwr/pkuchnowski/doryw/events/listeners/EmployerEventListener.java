package pl.edu.pwr.pkuchnowski.doryw.events.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.edu.pwr.pkuchnowski.doryw.events.EmployerEvent;
import pl.edu.pwr.pkuchnowski.doryw.services.EmailService;

@Component
@RequiredArgsConstructor
public class EmployerEventListener {
    private final EmailService emailService;

    @EventListener
    public void onEmployerEvent(EmployerEvent employerEvent) {
        switch (employerEvent.getType()) {
            case REGISTRATION:
                emailService.sendNewEmployerAccountEmail(
                        employerEvent.getEmployerEntity().getUserEntity().getFirstName(),
                        employerEvent.getEmployerEntity().getUserEntity().getEmail(),
                        employerEvent.getData().get("key").toString(),
                        employerEvent.getFrontendURL()
                );
                break;
            case DELETION:
                emailService.sendEmployerDeletionEmail(
                        employerEvent.getEmployerEntity().getUserEntity().getFirstName(),
                        employerEvent.getEmployerEntity().getUserEntity().getEmail(),
                        employerEvent.getFrontendURL()
                );
                break;
            default:
                break;
        }
    }
}