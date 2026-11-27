package com.pe.advanced.dao.impl;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractUpdatableEntity extends AbstractCreatableEntity {

    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamp(3)")
    private Instant updatedAt;

    /* Used by hibernate only */
    @Version
    private Integer version;

    protected AbstractUpdatableEntity() {
        // POJO
    }

    protected AbstractUpdatableEntity(UUID id) {
        super(id);
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Hibernate owns this column, so there is no setter and this reader is package private: the
     * only code with any business reading it is the DAO that compares it with what a caller
     * claims to have read.
     */
    Integer getVersion() {
        return version;
    }
}
