package com.pe.advanced.domain.asserter.product;

import com.pe.advanced.domain.asserter.AbstractUpdatableDeepEqualsAsserter;
import com.pe.advanced.domain.product.ProductSpecification;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductSpecificationAsserter extends AbstractUpdatableDeepEqualsAsserter<ProductSpecification> {

    public static final ProductSpecificationAsserter instance = new ProductSpecificationAsserter();

    private ProductSpecificationAsserter() {
        // Singleton
    }

    @Override
    public void assertDeepEquals(ProductSpecification expected, ProductSpecification actual) {
        super.assertDeepEquals(expected, actual);

        assertEquals(expected.getDimensions(), actual.getDimensions());
        assertEquals(expected.getWeight(), actual.getWeight());
    }
}
