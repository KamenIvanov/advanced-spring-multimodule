package com.pe.advanced.dao.api.category;

import com.pe.advanced.dao.api.search.SortBy;

public enum CategorySortByFields implements SortBy {

    NAME("name"),
    CREATED_AT("createdAt"),
    ACTIVE("active");

    private final String value;

    CategorySortByFields(String value) {
        this.value = value;
    }

    @Override
    public String getPropertyName() {
        return value;
    }
}
