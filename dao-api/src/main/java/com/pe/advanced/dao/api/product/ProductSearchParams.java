package com.pe.advanced.dao.api.product;

import com.pe.advanced.dao.api.search.AbstractParams;
import com.pe.advanced.domain.product.ProductStatus;

import java.util.Set;
import java.util.UUID;

public class ProductSearchParams extends AbstractParams<ProductSort> {

    private String name;
    private String sku;
    private UUID createdById;
    private Set<ProductStatus> statuses;

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

    public UUID getCreatedById() {
        return createdById;
    }

    public void setCreatedById(UUID createdById) {
        this.createdById = createdById;
    }

    public Set<ProductStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<ProductStatus> statuses) {
        this.statuses = statuses;
    }
}
