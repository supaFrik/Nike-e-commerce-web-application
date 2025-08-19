package vn.devpro.javaweb32.model.base;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // revisit if you move to Postgres
    private Long id;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    // If you wire AuditorAware<Long>, these can be Long and mapped as FKs in child entities if desired
    // @CreatedBy private Long createdByUserId;
    // @LastModifiedBy private Long updatedByUserId;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @PrePersist
    void prePersist() {
        // createdAt handled by auditing; keep only if you don't enable it
    }

    @PreUpdate
    void preUpdate() {
        // updatedAt handled by auditing
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    // Soft delete method
    public void softDelete() {
        this.deletedAt = Instant.now();
    }

    // Restore method
    public void restore() {
        this.deletedAt = null;
    }
}
