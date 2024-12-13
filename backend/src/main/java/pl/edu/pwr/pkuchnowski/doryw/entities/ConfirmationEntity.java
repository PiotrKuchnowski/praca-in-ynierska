package pl.edu.pwr.pkuchnowski.doryw.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Indexed;

import java.util.UUID;

@Entity
@Table(name = "confirmation", indexes = {
        @Index(name = "idx_confirmation_token", columnList = "token")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ConfirmationEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public ConfirmationEntity(UserEntity userEntity) {
        this.token = UUID.randomUUID().toString();
        this.userId = userEntity.getId();
    }

}
