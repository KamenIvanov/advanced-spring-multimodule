package com.pe.advanced.domain;

import java.time.Instant;
import java.util.UUID;

public abstract class AbstractCreatableDomain<IdType> {

    private IdType id;
    private Instant createdAt;
    private UUID createdById;

    protected AbstractCreatableDomain() {
        // POJO
    }

    protected AbstractCreatableDomain(IdType id) {
        this.id = id;
    }

    protected AbstractCreatableDomain(IdType id, Instant createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    protected AbstractCreatableDomain(IdType id, UUID createdById) {
        this.id = id;
        this.createdById = createdById;
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

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getCreatedById() {
        return createdById;
    }

    public void setCreatedById(UUID createdById) {
        this.createdById = createdById;
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
        final AbstractCreatableDomain<?> that = (AbstractCreatableDomain<?>) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + "]";
    }
}
