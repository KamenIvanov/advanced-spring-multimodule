package com.pe.advanced.domain.category;

import com.pe.advanced.domain.AbstractAuditable;

import java.time.Instant;
import java.util.UUID;

public class Category extends AbstractAuditable<UUID> {

    private String name;
    private CategoryStatus status = CategoryStatus.INACTIVE;

    public Category() {
        // POJO
    }

    public Category(UUID id, Instant createdAt, Instant updatedAt, CategoryStatus status) {
        super(id, createdAt, updatedAt);
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryStatus getStatus() {
        return status;
    }

    public void transitionTo(CategoryStatus nextStatus) {
        if (!this.status.canTransitionTo(nextStatus)) {
            throw new IllegalStateException("Business rule violated: Cannot transition from " + this.status + " to " + nextStatus);
        }
        this.status = nextStatus;
    }
}
