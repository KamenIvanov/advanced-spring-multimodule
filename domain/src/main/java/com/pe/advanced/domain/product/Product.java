package com.pe.advanced.domain.product;

import com.pe.advanced.domain.AbstractAuditable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Product extends AbstractAuditable<UUID> {

    private String name;
    private String sku;
    private BigDecimal price;
    private ProductStatus status = ProductStatus.DRAFT;

    // 1-1 relationship (Lifecycle bound)
    private ProductSpecification specification;

    public Product(UUID id, Instant createdAt, Instant updatedAt) {
        super(id, createdAt, updatedAt);
    }

    public Product(UUID id, UUID createdById, UUID updatedById, String name, String sku, BigDecimal price, ProductSpecification specification) {
        super(id, createdById, updatedById);
        this.name = Objects.requireNonNull(name);
        this.sku = Objects.requireNonNull(sku);
        this.price = Objects.requireNonNull(price);
        this.specification = Objects.requireNonNull(specification);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public ProductSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(ProductSpecification specification) {
        this.specification = specification;
    }

    public void transitionTo(ProductStatus nextStatus) {
        if (!this.status.canTransitionTo(nextStatus)) {
            throw new IllegalStateException("Business rule violated: Cannot transition from " + this.status + " to " + nextStatus);
        }
        this.status = nextStatus;
    }
}

