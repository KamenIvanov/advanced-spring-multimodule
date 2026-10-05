package com.pe.advanced.dao.impl.transformers.category;

import com.pe.advanced.dao.impl.category.assignment.CategoryAssignmentEntity;
import com.pe.advanced.domain.category.CategoryAssignment;
import com.pe.advanced.domain.transformers.AbstractBiTransformer;

public final class CategoryAssignmentTransformer extends AbstractBiTransformer<CategoryAssignmentEntity, CategoryAssignment> {

    public static final CategoryAssignmentTransformer instance = new CategoryAssignmentTransformer();

    private CategoryAssignmentTransformer() {
        // singleton
    }

    @Override
    public void copyToInput(CategoryAssignment domain, CategoryAssignmentEntity entity) {
        super.copyToInput(domain, entity);
        entity.setCategoryId(domain.getCategoryId());
        entity.setProductId(domain.getProductId());
        entity.setAssignedById(domain.getAssignedById());
    }

    @Override
    public CategoryAssignmentEntity createInput(CategoryAssignment domain) {
        if (domain == null) {
            return null;
        }

        final var entity = new CategoryAssignmentEntity();
        copyToInput(domain, entity);
        return entity;
    }

    @Override
    public CategoryAssignment createOutput(CategoryAssignmentEntity entity) {
        if (entity == null) {
            return null;
        }

        return new CategoryAssignment(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getCategoryId(),
                entity.getProductId(),
                entity.getAssignedById()
        );
    }
}
