package com.pe.advanced.dao.api.category;

import com.pe.advanced.dao.api.search.AbstractParams;
import com.pe.advanced.domain.category.CategoryStatus;

public class CategorySearchParams extends AbstractParams<CategorySort> {

    private String name;
    private CategoryStatus status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryStatus getStatus() {
        return status;
    }

    public void setStatus(CategoryStatus status) {
        this.status = status;
    }
}
