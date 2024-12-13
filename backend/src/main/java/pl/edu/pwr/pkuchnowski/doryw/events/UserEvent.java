package pl.edu.pwr.pkuchnowski.doryw.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.edu.pwr.pkuchnowski.doryw.entities.UserEntity;
import pl.edu.pwr.pkuchnowski.doryw.enumeration.EventType;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class UserEvent {
    private UserEntity userEntity;
    private EventType type;
    private Map<?,?> data;
    private String frontendURL;
}
