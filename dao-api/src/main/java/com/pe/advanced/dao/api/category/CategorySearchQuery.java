package com.pe.advanced.dao.api.category;

import com.pe.advanced.dao.api.search.AbstractParams;

import java.util.UUID;

public class CategorySearchQuery extends AbstractParams<UUID, CategorySortByFields> {

    private String name;
    private Boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
