package com.pe.advanced.domain.asserter.category;

import com.pe.advanced.domain.asserter.AbstractAuditableDeepEqualsAsserter;
import com.pe.advanced.domain.category.Category;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryAsserter extends AbstractAuditableDeepEqualsAsserter<Category> {

    public static final CategoryAsserter instance = new CategoryAsserter();

    private CategoryAsserter() {
        // Singleton
    }

    @Override
    public void assertDeepEquals(Category expected, Category actual) {
        super.assertDeepEquals(expected, actual);

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getStatus(), actual.getStatus());
    }
}
