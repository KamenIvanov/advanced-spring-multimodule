package com.pe.advanced.dao.impl.product;

import com.pe.advanced.dao.api.product.ProductDao;
import com.pe.advanced.dao.api.product.ProductSearchParams;
import com.pe.advanced.dao.impl.AbstractSearchableTestCase;
import com.pe.advanced.domain.asserter.DeepEqualsAsserter;
import com.pe.advanced.domain.asserter.product.ProductAsserter;
import com.pe.advanced.domain.product.Product;
import com.pe.advanced.domain.product.ProductSpecification;
import com.pe.advanced.domain.product.ProductStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

class ProductDaoTestCase extends AbstractSearchableTestCase<UUID, Product, ProductDao, ProductSearchParams> {

    @Autowired
    private ProductDao productDao;

    @Override
    protected ProductSearchParams createSearchParams() {
        return new ProductSearchParams();
    }

    @Override
    protected ProductDao getDao() {
        return productDao;
    }

    @Override
    public Product createDomain() {
        final var product = new Product();
        product.setCreatedById(UUID.randomUUID());
        product.setUpdatedById(UUID.randomUUID());
        product.setName("Simple product name");
        product.setSku(UUID.randomUUID().toString());
        product.setPrice(BigDecimal.TWO);
        product.setSpecification(new ProductSpecification("10x20x5", 3.50));
        return product;
    }

    @Override
    protected DeepEqualsAsserter<Product> getAsserter() {
        return ProductAsserter.instance;
    }

    @Override
    protected Product updateDomain(Product domain) {
        domain.setName("New name");
        return domain;
    }

    @Test
    void testLoadBySku() {
        final var product = runSave();
        final var productBySku = getDao().loadBySku(product.getSku());
        Assertions.assertNotNull(productBySku);
        getAsserter().assertDeepEquals(product, productBySku);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testLoadBySkuNullOrEmptySku(String sku) {
        final var productBySku = getDao().loadBySku(sku);
        Assertions.assertNull(productBySku);
    }
}
