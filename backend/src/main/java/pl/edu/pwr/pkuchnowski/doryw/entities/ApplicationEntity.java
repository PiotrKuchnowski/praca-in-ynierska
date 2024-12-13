package pl.edu.pwr.pkuchnowski.doryw.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "applications", indexes = {
        @Index(name = "idx_application_user_id", columnList = "user_id"),
        @Index(name = "idx_application_job_offer_id", columnList = "job_offer_id"),
        @Index(name = "idx_application_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ApplicationEntity extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "message", nullable = false)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", nullable = false, foreignKey = @ForeignKey(name = "fk_application_status"), referencedColumnName = "id")
    private ApplicationStatusEntity status;

    @Column(name = "response", nullable = true)
    private String response;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_application_user_id"))
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_offer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_application_job_offer_id"))
    private JobOfferEntity jobOffer;
}
