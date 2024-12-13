package pl.edu.pwr.pkuchnowski.doryw.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pwr.pkuchnowski.doryw.entities.UserEntity;
import pl.edu.pwr.pkuchnowski.doryw.repositories.UserRepository;

@Component
public class RequestContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static UserRepository userRepository;

    @Autowired
    private RequestContext(UserRepository userRepository) {
       RequestContext.userRepository = userRepository;
    }

    public static void start() {
        USER_ID.remove();
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    private static UserEntity user = null;

    public static void setUser(UserEntity user) {
        RequestContext.user = user;
        USER_ID.set(user.getId());
    }

    public static UserEntity getUser() {
        if(user != null) {
            return user;
        }

        if(getUserId() == null) {
            return getSystemUser();
        }
        return userRepository.findById(getUserId()).orElse(null);
    }

    public static UserEntity getSystemUser() {
        return userRepository.findById(0L).orElse(null);
    }
}
