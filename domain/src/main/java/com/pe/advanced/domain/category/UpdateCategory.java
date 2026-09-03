package com.pe.advanced.domain.category;

public class UpdateCategory {

    private boolean active;

    public UpdateCategory() {
        // POJO
    }

    public UpdateCategory(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
