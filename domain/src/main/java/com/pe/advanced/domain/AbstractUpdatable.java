package com.pe.advanced.domain;

import java.time.Instant;

public abstract class AbstractUpdatable<IdType> extends AbstractCreatable<IdType> {

    private final Instant updatedAt;

    protected AbstractUpdatable() {
        this.updatedAt = Instant.now();
    }

    protected AbstractUpdatable(IdType id) {
        super(id);
        this.updatedAt = super.getCreatedAt();
    }

    protected AbstractUpdatable(IdType id, Instant createdAt, Instant updatedAt) {
        super(id, createdAt);
        this.updatedAt = updatedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
