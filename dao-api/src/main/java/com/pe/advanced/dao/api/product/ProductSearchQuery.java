package com.pe.advanced.dao.api.product;

import com.pe.advanced.dao.api.search.AbstractParams;

import java.util.UUID;

public class ProductSearchQuery extends AbstractParams<UUID, ProductSort> {

    private String name;
    private String sku;

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
}
