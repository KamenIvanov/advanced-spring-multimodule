package com.pe.advanced.domain.product;

import com.pe.advanced.domain.AbstractNamedDomain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Product extends AbstractNamedDomain<UUID> {

    private String sku;
    private ProductStatus status;
    private BigDecimal price;

    // 1-1 relationship (Lifecycle bound)
    private ProductSpecification specifications;

    public Product() {
        // POJO
    }

    public Product(UUID id, UUID createdById, String name, String sku, BigDecimal price, ProductSpecification specifications) {
        super(id, createdById, name);
        this.sku = Objects.requireNonNull(sku);
        this.price = Objects.requireNonNull(price);
        this.specifications = Objects.requireNonNull(specifications);
        this.status = ProductStatus.DRAFT;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductSpecification getSpecifications() {
        return specifications;
    }

    public void setSpecifications(ProductSpecification specifications) {
        this.specifications = specifications;
    }

    public void transitionTo(ProductStatus nextStatus) {
        if (!this.status.canTransitionTo(nextStatus)) {
            throw new IllegalStateException("Business rule violated: Cannot transition from " + this.status + " to " + nextStatus);
        }
        this.status = nextStatus;
    }
}
