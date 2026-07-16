package com.pe.advanced.domain.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductDomainTestCase {

    private static final UUID CREATOR_ID = UUID.randomUUID();
    private static final ProductSpecification spec = new ProductSpecification(UUID.randomUUID(), "10x20x30", 1.5);

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(UUID.randomUUID(), CREATOR_ID, CREATOR_ID, "ThinkPad X1", "SKU-123", BigDecimal.valueOf(1999), spec);
    }

    // --- DRAFT Status Transitions ---

    @Test
    void shouldAllowTransitionFromDraftToActive() {
        product.transitionTo(ProductStatus.ACTIVE);
        assertEquals(ProductStatus.ACTIVE, product.getStatus());
    }

    @Test
    void shouldAllowTransitionFromDraftToArchived() {
        product.transitionTo(ProductStatus.ARCHIVED);
        assertEquals(ProductStatus.ARCHIVED, product.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenTransitioningFromDraftToOutOfStock() {
        assertThrows(IllegalStateException.class, () -> product.transitionTo(ProductStatus.OUT_OF_STOCK));
    }

    @Test
    void shouldThrowExceptionWhenTransitioningFromDraftToDraft() {
        assertThrows(IllegalStateException.class, () -> product.transitionTo(ProductStatus.DRAFT));
    }

    // --- ACTIVE Status Transitions ---

    @Test
    void shouldAllowTransitionFromActiveToOutOfStock() {
        product.transitionTo(ProductStatus.ACTIVE); // First transition to ACTIVE
        product.transitionTo(ProductStatus.OUT_OF_STOCK);
        assertEquals(ProductStatus.OUT_OF_STOCK, product.getStatus());
    }

    @Test
    void shouldAllowTransitionFromActiveToArchived() {
        product.transitionTo(ProductStatus.ACTIVE); // First transition to ACTIVE
        product.transitionTo(ProductStatus.ARCHIVED);
        assertEquals(ProductStatus.ARCHIVED, product.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenTransitioningFromActiveToDraft() {
        product.transitionTo(ProductStatus.ACTIVE); // First transition to ACTIVE
        assertThrows(IllegalStateException.class, () -> product.transitionTo(ProductStatus.DRAFT));
    }

    @Test
    void shouldThrowExceptionWhenTransitioningFromActiveToActive() {
        product.transitionTo(ProductStatus.ACTIVE); // First transition to ACTIVE
        assertThrows(IllegalStateException.class, () -> product.transitionTo(ProductStatus.ACTIVE));
    }

    // --- OUT_OF_STOCK Status Transitions ---

    @Test
    void shouldAllowTransitionFromOutOfStockToActive() {
        product.transitionTo(ProductStatus.ACTIVE); // DRAFT -> ACTIVE
        product.transitionTo(ProductStatus.OUT_OF_STOCK); // ACTIVE -> OUT_OF_STOCK
        product.transitionTo(ProductStatus.ACTIVE); // OUT_OF_STOCK -> ACTIVE
        assertEquals(ProductStatus.ACTIVE, product.getStatus());
    }

    @Test
    void shouldAllowTransitionFromOutOfStockToArchived() {
        product.transitionTo(ProductStatus.ACTIVE); // DRAFT -> ACTIVE
        product.transitionTo(ProductStatus.OUT_OF_STOCK); // ACTIVE -> OUT_OF_STOCK
        product.transitionTo(ProductStatus.ARCHIVED); // OUT_OF_STOCK -> ARCHIVED
        assertEquals(ProductStatus.ARCHIVED, product.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenTransitioningFromOutOfStockToDraft() {
        product.transitionTo(ProductStatus.ACTIVE); // DRAFT -> ACTIVE
        product.transitionTo(ProductStatus.OUT_OF_STOCK); // ACTIVE -> OUT_OF_STOCK
        assertThrows(IllegalStateException.class, () -> product.transitionTo(ProductStatus.DRAFT));
    }

    @Test
    void shouldThrowExceptionWhenTransitioningFromOutOfStockToOutOfStock() {
        product.transitionTo(ProductStatus.ACTIVE); // DRAFT -> ACTIVE
        product.transitionTo(ProductStatus.OUT_OF_STOCK); // ACTIVE -> OUT_OF_STOCK
        assertThrows(IllegalStateException.class, () -> product.transitionTo(ProductStatus.OUT_OF_STOCK));
    }

    // --- ARCHIVED Status Transitions ---

    @Test
    void shouldThrowExceptionWhenTransitioningFromArchivedToAnyState() {
        product.transitionTo(ProductStatus.ARCHIVED); // DRAFT -> ARCHIVED
        assertThrows(IllegalStateException.class, () -> product.transitionTo(ProductStatus.DRAFT));
        assertThrows(IllegalStateException.class, () -> product.transitionTo(ProductStatus.ACTIVE));
        assertThrows(IllegalStateException.class, () -> product.transitionTo(ProductStatus.OUT_OF_STOCK));
        assertThrows(IllegalStateException.class, () -> product.transitionTo(ProductStatus.ARCHIVED));
    }
}
