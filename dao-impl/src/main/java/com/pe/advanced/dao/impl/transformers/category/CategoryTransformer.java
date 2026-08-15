package com.pe.advanced.dao.impl.transformers.category;

import com.pe.advanced.dao.impl.category.CategoryEntity;
import com.pe.advanced.dao.impl.transformers.AbstractAuditableEntityTransformer;
import com.pe.advanced.domain.category.Category;

public class CategoryTransformer extends AbstractAuditableEntityTransformer<CategoryEntity, Category> {

    public static final CategoryTransformer instance = new CategoryTransformer();

    private CategoryTransformer() {
        // POJO
    }

    @Override
    public void copyToInput(Category dest, CategoryEntity source) {
        super.copyToInput(dest, source);

        source.setName(dest.getName());
        source.setActive(dest.isActive());
    }

    @Override
    public void copyToOutput(CategoryEntity source, Category dest) {
        super.copyToOutput(source, dest);

        dest.setName(source.getName());
        dest.setActive(source.isActive());
    }

    @Override
    public CategoryEntity createInput(Category category) {
        if (category == null) {
            return null;
        }

        final var entity = new CategoryEntity();
        copyToInput(category, entity);
        return entity;
    }

    @Override
    public Category createOutput(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        final var category = new Category(entity.getId(), entity.getCreatedAt(), entity.getUpdatedAt());
        copyToOutput(entity, category);
        return category;
    }
}
