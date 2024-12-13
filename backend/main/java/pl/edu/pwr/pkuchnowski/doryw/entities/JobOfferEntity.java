package pl.edu.pwr.pkuchnowski.doryw.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "job_offers", indexes = {
        @Index(name = "idx_job_offer_employer_id", columnList = "employer_id"),
        @Index(name = "idx_job_offer_pay_type_id", columnList = "pay_type_id"),
        @Index(name = "idx_job_offer_job_category_id", columnList = "job_category_id"),
        @Index(name = "idx_job_offer_start_date", columnList = "start_date"),
        @Index(name = "idx_job_offer_end_date", columnList = "end_date"),
        @Index(name = "idx_job_offer_title", columnList = "title"),
        @Index(name = "idx_job_offer_pay", columnList = "pay"),
        @Index(name = "idx_job_offer_description", columnList = "description"),
        @Index(name = "idx_job_offer_reference_id", columnList = "reference_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class JobOfferEntity extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_job_offer_employer_id"))
    private EmployerEntity employer;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "pay", nullable = false)
    private Double pay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_type_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_job_offer_pay_type_id"))
    private PayTypeEntity payType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_category_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_job_offer_job_category_id"))
    private JobCategoryEntity jobCategory;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "job_offer_location",
            joinColumns = @JoinColumn(name = "job_offer_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "location_id", referencedColumnName = "id"),
            foreignKey = @ForeignKey(name = "fk_job_offer_location_job_offer_id"),
            inverseForeignKey = @ForeignKey(name = "fk_job_offer_location_location_id"),
            indexes = {
                    @Index(name = "idx_job_offer_location_job_offer_id", columnList = "job_offer_id"),
                    @Index(name = "idx_job_offer_location_location_id", columnList = "location_id")
            }
    )
    private Set<LocationEntity> locations;
}
