package com.pe.advanced.domain.category;

import com.pe.advanced.domain.AbstractAuditable;

import java.time.Instant;
import java.util.UUID;

public class Category extends AbstractAuditable<UUID> {

    private String name;
    private boolean active;

    public Category() {
       // POJO
    }

    public Category(UUID id, Instant createdAt, Instant updatedAt) {
        super(id, createdAt, updatedAt);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
