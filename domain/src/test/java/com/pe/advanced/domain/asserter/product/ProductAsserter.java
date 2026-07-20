package com.pe.advanced.domain.asserter.product;

import com.pe.advanced.domain.asserter.AbstractAuditableDeepEqualsAsserter;
import com.pe.advanced.domain.product.Product;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductAsserter extends AbstractAuditableDeepEqualsAsserter<Product> {

    public static final ProductAsserter instance = new ProductAsserter();

    private ProductAsserter() {
        // Singleton
    }

    @Override
    public void assertDeepEquals(Product expected, Product actual) {
        super.assertDeepEquals(expected, actual);

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSku(), actual.getSku());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(0, expected.getPrice().compareTo(actual.getPrice()));

        ProductSpecificationAsserter.instance.assertDeepEquals(expected.getSpecification(), actual.getSpecification());
    }
}
