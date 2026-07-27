package com.pe.advanced.dao.api.category;

import com.pe.advanced.dao.api.search.AbstractParams;

public class CategorySearchParams extends AbstractParams<CategorySortByFields> {

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
