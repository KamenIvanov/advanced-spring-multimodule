package com.pe.advanced.dao.impl;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.util.UUID;

@MappedSuperclass
public abstract class AbstractAuditableEntity extends AbstractUpdatableEntity {

    @Column(name = "created_by_id", nullable = false)
    private UUID createdById;

    @Column(name = "updated_by_id", nullable = false)
    private UUID updatedById;

    protected AbstractAuditableEntity() {
        // POJO
    }

    protected AbstractAuditableEntity(UUID id) {
        super(id);
    }

    public UUID getCreatedById() {
        return createdById;
    }

    public void setCreatedById(UUID createdById) {
        this.createdById = createdById;
    }

    public UUID getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(UUID updatedById) {
        this.updatedById = updatedById;
    }
}
