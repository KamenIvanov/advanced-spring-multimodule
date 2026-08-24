package com.pe.advanced.domain.product;

import com.pe.advanced.domain.AbstractUpdatable;

import java.time.Instant;
import java.util.UUID;

public class ProductSpecification extends AbstractUpdatable<UUID> {

    private String dimensions;
    private double weight;

    public ProductSpecification(UUID id, Instant createdAt, Instant updatedAt) {
        super(id, createdAt, updatedAt);
    }

    public ProductSpecification(String dimensions, double weight) {
        this.dimensions = dimensions;
        this.weight = weight;
    }

    public ProductSpecification(UUID id, String dimensions, double weight) {
        super(id);
        this.dimensions = dimensions;
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
