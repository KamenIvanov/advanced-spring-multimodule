package com.pe.advanced.domain;

import java.time.Instant;
import java.util.UUID;

public abstract class AbstractUpdatableDomain<IdType> extends AbstractCreatableDomain<IdType> {

    private Instant updatedAt;

    protected AbstractUpdatableDomain() {
        // POJO
    }

    protected AbstractUpdatableDomain(IdType id) {
        setId(id);
    }

    protected AbstractUpdatableDomain(IdType id, UUID createdById) {
        super(id, createdById);
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
