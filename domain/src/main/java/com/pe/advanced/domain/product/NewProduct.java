package com.pe.advanced.domain.product;

import java.math.BigDecimal;
import java.util.Objects;

public class NewProduct {

    private String name;
    private String sku;
    private BigDecimal price;

    private String dimensions;
    private double weight;

    public NewProduct() {
        // POJO
    }

    public NewProduct(String name, String sku, BigDecimal price, String dimensions, double weight) {
        this.name = Objects.requireNonNull(name);
        this.sku = Objects.requireNonNull(sku);
        this.price = Objects.requireNonNull(price);
        this.dimensions = Objects.requireNonNull(dimensions);
        this.weight = weight;
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
