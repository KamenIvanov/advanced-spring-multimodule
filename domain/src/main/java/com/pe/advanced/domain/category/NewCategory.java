package com.pe.advanced.domain.category;

import java.util.Objects;

public class NewCategory {

    private String name;
    private boolean active;

    public NewCategory() {
        // POJO
    }

    public NewCategory(String name, boolean active) {
        this.name = Objects.requireNonNull(name);
        this.active = active;
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
