package com.pe.advanced.domain;

import java.time.Instant;
import java.util.Objects;

public abstract class AbstractCreatable<IdType> {

    private IdType id;
    private final Instant createdAt;

    protected AbstractCreatable() {
        this.createdAt = Instant.now();
    }

    protected AbstractCreatable(IdType id) {
        this(id, Instant.now());
    }

    protected AbstractCreatable(IdType id, Instant createdAt) {
        this.id = Objects.requireNonNull(id);
        this.createdAt = createdAt;
    }

    public IdType getId() {
        return id;
    }

    public void setId(IdType id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AbstractCreatable<?> that = (AbstractCreatable<?>) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + "]";
    }
}
