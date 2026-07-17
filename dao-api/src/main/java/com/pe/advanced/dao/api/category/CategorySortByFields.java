package com.pe.advanced.dao.api.category;

import com.pe.advanced.dao.api.SortableEnum;

public enum CategorySortByFields implements SortableEnum {

    NAME("name"),
    CREATED_AT("createdAt"),
    ACTIVE("active");

    private final String value;

    CategorySortByFields(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
