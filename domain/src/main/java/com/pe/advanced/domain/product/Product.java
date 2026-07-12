package com.pe.advanced.domain.product;

import com.pe.advanced.domain.AbstractNamedDomain;

import java.math.BigDecimal;
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
}
