package com.pe.advanced.domain.category;

import com.pe.advanced.domain.AbstractCreatable;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class CategoryAssignment extends AbstractCreatable<UUID> {

    private final UUID productId;
    private final UUID categoryId;
    private final UUID assignedById;

    public CategoryAssignment(UUID categoryId, UUID productId, UUID assignedById) {
        super();
        this.categoryId = Objects.requireNonNull(categoryId, "categoryId is mandatory");
        this.productId = Objects.requireNonNull(productId, "productId is mandatory");
        this.assignedById = Objects.requireNonNull(assignedById, "assignedById is mandatory");
    }

    public CategoryAssignment(UUID id, Instant createdAt, UUID categoryId, UUID productId, UUID assignedById) {
        super(id, createdAt);
        this.categoryId = Objects.requireNonNull(categoryId, "categoryId is mandatory");
        this.productId = Objects.requireNonNull(productId, "productId is mandatory");
        this.assignedById = Objects.requireNonNull(assignedById, "assignedById is mandatory");
    }

    public UUID getProductId() {
        return productId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public UUID getAssignedById() {
        return assignedById;
    }
}
