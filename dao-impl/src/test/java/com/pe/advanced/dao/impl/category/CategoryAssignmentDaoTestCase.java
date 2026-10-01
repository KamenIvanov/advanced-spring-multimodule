package com.pe.advanced.dao.impl.category;

import com.pe.advanced.dao.api.category.assignment.CategoryAssignmentDao;
import com.pe.advanced.dao.impl.AbstractDaoTest;
import com.pe.advanced.domain.category.CategoryAssignment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryAssignmentDaoTestCase extends AbstractDaoTest {

    private final UUID categoryId = UUID.randomUUID();
    private final UUID assignedById = UUID.randomUUID();

    @Autowired
    private CategoryAssignmentDao dao;

    // --- saveAll ---

    @Test
    void testSaveAllReturnsPersistedInstancesInInputOrder() {
        final var first = assignment(categoryId, UUID.randomUUID());
        final var second = assignment(categoryId, UUID.randomUUID());

        final var saved = dao.saveAll(List.of(first, second));

        assertEquals(2, saved.size());
        assertEquals(first.getProductId(), saved.get(0).getProductId());
        assertEquals(second.getProductId(), saved.get(1).getProductId());
        assertNotNull(saved.get(0).getId());
        assertNotNull(saved.get(0).getCreatedAt());
        assertNull(first.getId(), "the transient input must not be mutated");
    }

    @Test
    void testSaveAllMapsEveryId() {
        final var productId = UUID.randomUUID();

        final var saved = dao.saveAll(List.of(assignment(categoryId, productId))).getFirst();

        assertEquals(categoryId, saved.getCategoryId());
        assertEquals(productId, saved.getProductId());
        assertEquals(assignedById, saved.getAssignedById());
    }

    @Test
    void testSaveAllRejectsAnAlreadyPersistedInstance() {
        final var persisted = dao.saveAll(List.of(assignment(categoryId, UUID.randomUUID()))).getFirst();

        final var ex = assertThrows(IllegalArgumentException.class, () -> dao.saveAll(List.of(persisted)));
        assertEquals("Assignment existing!", ex.getMessage());
    }

    @Test
    void testEmptyCollections() {
        assertTrue(dao.saveAll(List.of()).isEmpty());
        assertTrue(dao.findAssignedProductIds(categoryId, List.of()).isEmpty());
    }

    // --- findAssignedProductIds ---

    @Test
    void testFindAssignedReturnsOnlyAssignedAmongRequested() {
        final var assigned = UUID.randomUUID();
        final var unassigned = UUID.randomUUID();
        dao.saveAll(List.of(assignment(categoryId, assigned), assignment(categoryId, UUID.randomUUID())));

        assertEquals(Set.of(assigned), dao.findAssignedProductIds(categoryId, List.of(assigned, unassigned)));
    }

    @Test
    void testFindAssignedDoesNotLeakAcrossCategories() {
        final var productId = UUID.randomUUID();
        dao.saveAll(List.of(assignment(UUID.randomUUID(), productId)));

        assertTrue(dao.findAssignedProductIds(categoryId, List.of(productId)).isEmpty());
    }

    // --- delete (single pair) ---

    @Test
    void testDeleteRemovesOnlyTheTargetedPair() {
        final var kept = UUID.randomUUID();
        final var removed = UUID.randomUUID();
        final var otherCategory = UUID.randomUUID();
        dao.saveAll(List.of(
                assignment(categoryId, kept),
                assignment(categoryId, removed),
                assignment(otherCategory, removed)));

        dao.delete(categoryId, removed);

        assertEquals(Set.of(kept), dao.findAssignedProductIds(categoryId, List.of(kept, removed)));
        assertEquals(Set.of(removed), dao.findAssignedProductIds(otherCategory, List.of(removed)));
    }

    @Test
    void testDeleteMissingIsNoOp() {
        assertDoesNotThrow(() -> dao.delete(categoryId, UUID.randomUUID()));
    }

    // --- bulk cleanup ---

    @Test
    void testDeleteAllForCategoryRemovesOnlyThatCategory() {
        final var other = UUID.randomUUID();
        final var inBoth = UUID.randomUUID();
        final var onlyHere = UUID.randomUUID();
        dao.saveAll(List.of(
                assignment(categoryId, inBoth),
                assignment(categoryId, onlyHere),
                assignment(other, inBoth)));

        dao.deleteAllForCategory(categoryId);

        assertTrue(dao.findAssignedProductIds(categoryId, List.of(inBoth, onlyHere)).isEmpty());
        assertEquals(Set.of(inBoth), dao.findAssignedProductIds(other, List.of(inBoth)));
    }

    @Test
    void testDeleteAllForProductRemovesItFromEveryCategory() {
        final var other = UUID.randomUUID();
        final var removed = UUID.randomUUID();
        final var kept = UUID.randomUUID();
        dao.saveAll(List.of(
                assignment(categoryId, removed),
                assignment(other, removed),
                assignment(categoryId, kept)));

        dao.deleteAllForProduct(removed);

        assertTrue(dao.findAssignedProductIds(categoryId, List.of(removed)).isEmpty());
        assertTrue(dao.findAssignedProductIds(other, List.of(removed)).isEmpty());
        assertEquals(Set.of(kept), dao.findAssignedProductIds(categoryId, List.of(kept)));
    }

    @Test
    void testBulkDeletesAreNoOpsWhenNothingMatches() {
        assertDoesNotThrow(() -> dao.deleteAllForCategory(UUID.randomUUID()));
        assertDoesNotThrow(() -> dao.deleteAllForProduct(UUID.randomUUID()));
    }

    // --- constraint ---

    @Test
    void testDuplicatePairViolatesUniqueConstraint() {
        final var productId = UUID.randomUUID();
        dao.saveAll(List.of(assignment(categoryId, productId)));

        // keep this last: once the flush has failed, the test's transaction is unusable
        assertThrows(DataIntegrityViolationException.class, () -> dao.saveAll(List.of(assignment(categoryId, productId))));
    }

    private CategoryAssignment assignment(UUID category, UUID product) {
        return new CategoryAssignment(category, product, assignedById);
    }
}
