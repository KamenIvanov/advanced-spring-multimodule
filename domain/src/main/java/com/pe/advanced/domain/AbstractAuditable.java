package com.pe.advanced.domain;

import java.util.UUID;

public abstract class AbstractAuditable<IdType> extends AbstractUpdatable<IdType> {

    private UUID createdById;
    private UUID updatedById;

    protected AbstractAuditable() {
        // POJO
    }

    protected AbstractAuditable(IdType id) {
        super(id);
    }

    protected AbstractAuditable(IdType id, UUID createdById, UUID updatedById) {
        super(id);
        this.createdById = createdById;
        this.updatedById = updatedById;
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
