package pl.edu.pwr.pkuchnowski.doryw.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.AlternativeJdkIdGenerator;
import pl.edu.pwr.pkuchnowski.doryw.domain.RequestContext;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public abstract class Auditable {

    @Column(name = "reference_id", nullable = false, unique = true, updatable = false)
    private String referenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "created_by",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_created_by", value = ConstraintMode.CONSTRAINT),
            nullable = false
    )
    private UserEntity createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "updated_by",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_updated_by", value = ConstraintMode.CONSTRAINT),
            nullable = false
    )
    private UserEntity updatedBy;

    @NotNull
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @CreatedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist() {
        if(getCreatedBy() == null) {
            setCreatedBy(RequestContext.getUser());
        }
        if(getUpdatedBy() == null) {
            setUpdatedBy(RequestContext.getUser());
        }

        setUpdatedAt(LocalDateTime.now());
        setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate() {
        UserEntity user;

        user = RequestContext.getUser();

        setUpdatedAt(LocalDateTime.now());
        setUpdatedBy(user);
    }

    public Auditable() {
        this.referenceId = new AlternativeJdkIdGenerator().generateId().toString();
    }

}
