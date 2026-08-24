package com.pe.advanced.dao.impl.product;

import com.pe.advanced.dao.impl.AbstractAuditableEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCTS")
public class ProductEntity extends AbstractAuditableEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sku", length = 256, nullable = false, unique = true)
    private String sku;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatusEntity status;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "specification_id", referencedColumnName = "id", nullable = true)
    private ProductSpecificationEntity specification;

    public ProductEntity() {
        // POJO
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

    public ProductStatusEntity getStatus() {
        return status;
    }

    public void setStatus(ProductStatusEntity status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductSpecificationEntity getSpecification() {
        return specification;
    }

    public void setSpecification(ProductSpecificationEntity specification) {
        this.specification = specification;
    }
}
