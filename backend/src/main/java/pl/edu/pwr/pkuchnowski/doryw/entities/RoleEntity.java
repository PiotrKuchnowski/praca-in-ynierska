package pl.edu.pwr.pkuchnowski.doryw.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import pl.edu.pwr.pkuchnowski.doryw.enumeration.Authority;

@Entity
@Table(name = "roles", indexes = {
        @Index(name = "idx_role_name", columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RoleEntity extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String name;

    public RoleEntity(String name) {
        this.name = name;
    }
}
