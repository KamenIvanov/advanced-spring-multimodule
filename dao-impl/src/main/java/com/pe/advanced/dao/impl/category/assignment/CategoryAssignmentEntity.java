package com.pe.advanced.dao.impl.category.assignment;

import com.pe.advanced.dao.impl.AbstractCreatableEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "CATEGORY_ASSIGNMENTS",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_category_assignments_category_product",
                columnNames = {"category_id", "product_id"}
        ),
        indexes = @Index(
                name = "idx_category_assignments_product",
                columnList = "product_id"
        )
)
public class CategoryAssignmentEntity extends AbstractCreatableEntity {

    @Column(name = "category_id", nullable = false, updatable = false)
    private UUID categoryId;

    @Column(name = "product_id", nullable = false, updatable = false)
    private UUID productId;

    @Column(name = "assigned_by_id", nullable = false, updatable = false)
    private UUID assignedById;

    public CategoryAssignmentEntity() {
        // POJO
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getAssignedById() {
        return assignedById;
    }

    public void setAssignedById(UUID assignedById) {
        this.assignedById = assignedById;
    }
}
