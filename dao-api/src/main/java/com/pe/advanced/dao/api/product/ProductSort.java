package com.pe.advanced.dao.api.product;

import com.pe.advanced.dao.api.search.SortBy;

public enum ProductSort implements SortBy {

    PRICE("price"),
    NAME("name"),
    CREATED_AT("createdAt");

    private final String propertyName;

    ProductSort(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }
}
