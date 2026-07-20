package com.pe.advanced.dao.impl;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractUpdatableEntity extends AbstractCreatableEntity {

    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamp(3)")
    private Instant updatedAt;

    protected AbstractUpdatableEntity() {
      this.updatedAt = Instant.now();
    }

    protected AbstractUpdatableEntity(UUID id) {
        super(id);
        this.updatedAt = Instant.now();
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
