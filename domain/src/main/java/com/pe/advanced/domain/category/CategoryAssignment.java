package com.pe.advanced.domain.category;

import com.pe.advanced.domain.AbstractCreatable;

import java.util.UUID;

public class CategoryAssignment extends AbstractCreatable<UUID> {

    private UUID productId;
    private UUID categoryId;
    private UUID assignedById;

    public CategoryAssignment() {
        // POJO
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public UUID getAssignedById() {
        return assignedById;
    }

    public void setAssignedById(UUID assignedById) {
        this.assignedById = assignedById;
    }
}
