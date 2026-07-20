package com.pe.advanced.dao.impl.product;

import com.pe.advanced.dao.impl.AbstractUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "PRODUCT_SPECIFICATION")
public class ProductSpecificationEntity extends AbstractUpdatableEntity {

    @Column(name = "dimension", length = 16, nullable = false)
    @NotNull
    private String dimension;

    @Column(name = "weight", nullable = false)
    private double weight;

    public ProductSpecificationEntity() {
        // POJO
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
