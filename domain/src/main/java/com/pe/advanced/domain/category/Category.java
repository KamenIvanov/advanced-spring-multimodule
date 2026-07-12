package com.pe.advanced.domain.category;

import com.pe.advanced.domain.AbstractNamedDomain;

import java.util.UUID;

public class Category extends AbstractNamedDomain<UUID> {

    private boolean active;

    public Category() {
        // POJO
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
