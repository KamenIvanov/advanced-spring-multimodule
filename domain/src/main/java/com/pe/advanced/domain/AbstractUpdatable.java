package com.pe.advanced.domain;

import java.time.Instant;

public abstract class AbstractUpdatable<IdType> extends AbstractCreatable<IdType> {

    private Instant updatedAt;

    protected AbstractUpdatable() {
        this.updatedAt = Instant.now();
    }

    protected AbstractUpdatable(IdType id) {
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
