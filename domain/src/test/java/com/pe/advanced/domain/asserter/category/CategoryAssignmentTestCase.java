package com.pe.advanced.domain.asserter.category;

import com.pe.advanced.domain.category.CategoryAssignment;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryAssignmentTestCase {

    private final UUID id = UUID.randomUUID();
    private final UUID categoryId = UUID.randomUUID();
    private final UUID productId = UUID.randomUUID();
    private final UUID assignedById = UUID.randomUUID();

    @Test
    void testTransientConstructorMapsEveryId() {
        final var assignment = new CategoryAssignment(categoryId, productId, assignedById);

        assertNull(assignment.getId());
        assertEquals(categoryId, assignment.getCategoryId());
        assertEquals(productId, assignment.getProductId());
        assertEquals(assignedById, assignment.getAssignedById());
    }

    @Test
    void testPersistedConstructorMapsEveryId() {
        final var createdAt = Instant.now();
        final var assignment = new CategoryAssignment(id, createdAt, categoryId, productId, assignedById);

        assertEquals(id, assignment.getId());
        assertEquals(createdAt, assignment.getCreatedAt());
        assertEquals(categoryId, assignment.getCategoryId());
        assertEquals(productId, assignment.getProductId());
        assertEquals(assignedById, assignment.getAssignedById());
    }

    @Test
    void testNullsAreRejected() {
        assertThrows(NullPointerException.class, () -> new CategoryAssignment(null, productId, assignedById));
        assertThrows(NullPointerException.class, () -> new CategoryAssignment(categoryId, null, assignedById));
        assertThrows(NullPointerException.class, () -> new CategoryAssignment(categoryId, productId, null));
    }
}
