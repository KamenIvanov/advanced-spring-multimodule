package com.pe.advanced.dao.impl.proxy;

import com.pe.advanced.dao.api.product.ProductAssignmentInfo;
import com.pe.advanced.dao.api.product.ProductDao;
import com.pe.advanced.dao.api.product.ProductSearchParams;
import com.pe.advanced.domain.product.Product;
import jakarta.persistence.EntityManager;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class ProductsDaoProxy extends SearchableCrudDaoProxy<UUID, Product, ProductSearchParams, ProductDao> implements ProductDao {

    public ProductsDaoProxy(ProductDao proxied, EntityManager entityManager) {
        super(proxied, entityManager);
    }

    @Override
    public Product loadBySku(String sku) {
        return proxied.loadBySku(sku);
    }

    @Override
    public Map<UUID, ProductAssignmentInfo> findAssignmentInfo(Collection<UUID> ids) {
        return proxied.findAssignmentInfo(ids);
    }
}
