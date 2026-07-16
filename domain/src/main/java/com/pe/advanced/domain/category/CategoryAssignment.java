package com.pe.advanced.domain.category;

import com.pe.advanced.domain.AbstractCreatable;

import java.time.Instant;
import java.util.UUID;

public class CategoryAssignment extends AbstractCreatable<UUID> {

    private UUID productId;
    private UUID categoryId;

    public CategoryAssignment() {
        // POJO
    }

    public CategoryAssignment(UUID id, UUID productId, UUID categoryId) {
        super(id);
        this.productId = productId;
        this.categoryId = categoryId;
    }

    public CategoryAssignment(UUID id, Instant createdAt, UUID productId, UUID categoryId) {
        super(id, createdAt);
        this.productId = productId;
        this.categoryId = categoryId;
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
}
