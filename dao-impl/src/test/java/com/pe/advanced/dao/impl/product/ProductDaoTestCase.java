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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductDaoTestCase extends AbstractSearchableTestCase<UUID, Product, ProductDao, ProductSearchParams> {

    @Autowired
    private ProductDao productDao;

    @Test
    void testFindAssignmentInfo() {
        final var firstCreator = UUID.randomUUID();
        final var secondCreator = UUID.randomUUID();
        final var first = createDomainWithUniqueData();
        first.setCreatedById(firstCreator);
        final var second = createDomainWithUniqueData();
        second.setCreatedById(secondCreator);
        final var firstId = runSave(first).getId();
        final var secondId = runSave(second).getId();

        final var info = getDao().findAssignmentInfo(List.of(firstId, secondId, UUID.randomUUID()));

        assertEquals(2, info.size());
        assertEquals(firstCreator, info.get(firstId).createdById());
        assertEquals(secondCreator, info.get(secondId).createdById());
        assertEquals(ProductStatus.DRAFT, info.get(firstId).status());
        assertTrue(getDao().findAssignmentInfo(List.of()).isEmpty());
    }

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
