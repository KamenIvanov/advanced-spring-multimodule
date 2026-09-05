package com.pe.advanced.domain.category;

import java.util.Objects;

public class NewCategory {

    private String name;

    public NewCategory() {
        // POJO
    }

    public NewCategory(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
