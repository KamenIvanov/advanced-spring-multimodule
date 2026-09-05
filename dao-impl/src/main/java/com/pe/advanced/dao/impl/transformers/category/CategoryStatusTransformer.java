package com.pe.advanced.dao.impl.transformers.category;

import com.pe.advanced.dao.impl.category.CategoryStatusEntity;
import com.pe.advanced.domain.category.CategoryStatus;
import com.pe.advanced.domain.transformers.AbstractEnumTransformer;

public class CategoryStatusTransformer extends AbstractEnumTransformer<CategoryStatusEntity, CategoryStatus> {

    public static final CategoryStatusTransformer instance = new CategoryStatusTransformer();

    private CategoryStatusTransformer() {
        super(CategoryStatusEntity.class, CategoryStatus.class);
    }
}
