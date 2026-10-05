package com.pe.advanced.bl.service.categories;

import com.pe.advanced.bl.service.ProductAssignmentResult;
import com.pe.advanced.bl.service.impl.category.assignments.CategoryAssignmentsServiceImpl;
import com.pe.advanced.dao.api.category.CategoryDao;
import com.pe.advanced.dao.api.category.assignment.CategoryAssignmentDao;
import com.pe.advanced.dao.api.product.ProductAssignmentInfo;
import com.pe.advanced.dao.api.product.ProductDao;
import com.pe.advanced.domain.category.Category;
import com.pe.advanced.domain.category.CategoryAssignment;
import com.pe.advanced.domain.category.CategoryStatus;
import com.pe.advanced.domain.exceptions.AuthorizationException;
import com.pe.advanced.domain.exceptions.ConflictException;
import com.pe.advanced.domain.exceptions.NotFoundException;
import com.pe.advanced.domain.product.Product;
import com.pe.advanced.domain.product.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryAssignmentsServiceTestCase {

    private static final UUID REQUESTER = UUID.randomUUID();
    private static final UUID CATEGORY_ID = UUID.randomUUID();

    @Mock
    private CategoryAssignmentDao assignmentDao;
    @Mock
    private CategoryDao categoryDao;
    @Mock
    private ProductDao productDao;
    @Captor
    private ArgumentCaptor<Collection<CategoryAssignment>> savedCaptor;

    private CategoryAssignmentsServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CategoryAssignmentsServiceImpl(assignmentDao, categoryDao, productDao);
    }

    @Test
    void testMixedBatchReportsEveryOutcomeInRequestOrder() {
        final var fresh = UUID.randomUUID();
        final var already = UUID.randomUUID();
        final var foreign = UUID.randomUUID();
        final var unknown = UUID.randomUUID();
        final var archived = UUID.randomUUID();
        when(categoryDao.loadById(CATEGORY_ID)).thenReturn(category(CategoryStatus.ACTIVE));
        when(productDao.findAssignmentInfo(any())).thenReturn(Map.of(
                fresh, info(REQUESTER, ProductStatus.ACTIVE),
                already, info(REQUESTER, ProductStatus.ACTIVE),
                foreign, info(UUID.randomUUID(), ProductStatus.ACTIVE),
                archived, info(REQUESTER, ProductStatus.ARCHIVED)));
        when(assignmentDao.findAssignedProductIds(eq(CATEGORY_ID), any())).thenReturn(Set.of(already));
        when(assignmentDao.saveAll(any())).thenAnswer(inv -> persisted(inv.getArgument(0)));

        final var results = service.assignProducts(CATEGORY_ID, List.of(fresh, already, foreign, unknown, archived, fresh), REQUESTER);

        assertEquals(5, results.size(), "the repeated id is reported once");
        final var assigned = assertInstanceOf(ProductAssignmentResult.Assigned.class, results.get(0));
        assertEquals(fresh, assigned.assignment().getProductId());
        assertInstanceOf(ProductAssignmentResult.AlreadyAssigned.class, results.get(1));
        assertInstanceOf(ProductAssignmentResult.ProductNotFound.class, results.get(2));
        assertInstanceOf(ProductAssignmentResult.ProductNotFound.class, results.get(3));
        assertInstanceOf(ProductAssignmentResult.ProductArchived.class, results.get(4));

        verify(assignmentDao).findAssignedProductIds(CATEGORY_ID, Set.of(fresh, already));
        verify(assignmentDao).saveAll(savedCaptor.capture());
        assertEquals(1, savedCaptor.getValue().size());
        final var written = savedCaptor.getValue().iterator().next();
        assertEquals(fresh, written.getProductId());
        assertEquals(REQUESTER, written.getAssignedById());
    }

    @Test
    void testForeignProductIsIndistinguishableFromAMissingOne() {
        final var foreign = UUID.randomUUID();
        final var missing = UUID.randomUUID();
        when(categoryDao.loadById(CATEGORY_ID)).thenReturn(category(CategoryStatus.ACTIVE));
        when(productDao.findAssignmentInfo(any())).thenReturn(Map.of(foreign, info(UUID.randomUUID(), ProductStatus.ACTIVE)));
        when(assignmentDao.findAssignedProductIds(eq(CATEGORY_ID), any())).thenReturn(Set.of());
        when(assignmentDao.saveAll(any())).thenReturn(List.of());

        final var results = service.assignProducts(CATEGORY_ID, List.of(foreign, missing), REQUESTER);

        assertInstanceOf(ProductAssignmentResult.ProductNotFound.class, results.get(0));
        assertInstanceOf(ProductAssignmentResult.ProductNotFound.class, results.get(1));
    }

    @Test
    void testArchivedProductIsReportedBeforeAlreadyAssigned() {
        final var productId = UUID.randomUUID();
        when(categoryDao.loadById(CATEGORY_ID)).thenReturn(category(CategoryStatus.ACTIVE));
        when(productDao.findAssignmentInfo(any())).thenReturn(Map.of(productId, info(REQUESTER, ProductStatus.ARCHIVED)));
        when(assignmentDao.findAssignedProductIds(eq(CATEGORY_ID), any())).thenReturn(Set.of());
        when(assignmentDao.saveAll(any())).thenReturn(List.of());

        final var results = service.assignProducts(CATEGORY_ID, List.of(productId), REQUESTER);

        // An archived product is never queried for existing assignments, so the reason
        // reported is the one that actually blocked it.
        assertInstanceOf(ProductAssignmentResult.ProductArchived.class, results.getFirst());
        verify(assignmentDao).findAssignedProductIds(CATEGORY_ID, Set.of());
    }

    @Test
    void testEmptyProductListReturnsEmptyResultWithoutWriting() {
        when(categoryDao.loadById(CATEGORY_ID)).thenReturn(category(CategoryStatus.ACTIVE));
        when(productDao.findAssignmentInfo(any())).thenReturn(Map.of());
        when(assignmentDao.findAssignedProductIds(eq(CATEGORY_ID), any())).thenReturn(Set.of());
        when(assignmentDao.saveAll(any())).thenReturn(List.of());

        assertTrue(service.assignProducts(CATEGORY_ID, List.of(), REQUESTER).isEmpty());
    }

    @Test
    void testArchivedCategoryRejectsTheWholeRequest() {
        when(categoryDao.loadById(CATEGORY_ID)).thenReturn(category(CategoryStatus.ARCHIVED));
        assertThrows(ConflictException.class, () -> service.assignProducts(CATEGORY_ID, List.of(UUID.randomUUID()), REQUESTER));
        verify(assignmentDao, never()).saveAll(any());
    }

    @Test
    void testInactiveCategoryAcceptsProducts() {
        final var productId = UUID.randomUUID();
        when(categoryDao.loadById(CATEGORY_ID)).thenReturn(category(CategoryStatus.INACTIVE));
        when(productDao.findAssignmentInfo(any())).thenReturn(Map.of(productId, info(REQUESTER, ProductStatus.ACTIVE)));
        when(assignmentDao.findAssignedProductIds(eq(CATEGORY_ID), any())).thenReturn(Set.of());
        when(assignmentDao.saveAll(any())).thenAnswer(inv -> persisted(inv.getArgument(0)));

        final var results = service.assignProducts(CATEGORY_ID, List.of(productId), REQUESTER);

        assertInstanceOf(ProductAssignmentResult.Assigned.class, results.getFirst());
    }

    @Test
    void testUnknownCategory() {
        when(categoryDao.loadById(CATEGORY_ID)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> service.assignProducts(CATEGORY_ID, List.of(UUID.randomUUID()), REQUESTER));
        assertThrows(NotFoundException.class, () -> service.unassignProduct(CATEGORY_ID, UUID.randomUUID(), REQUESTER));
    }

    @Test
    void testNullRequester() {
        assertThrows(AuthorizationException.class, () -> service.assignProducts(CATEGORY_ID, List.of(UUID.randomUUID()), null));
        assertThrows(AuthorizationException.class, () -> service.unassignProduct(CATEGORY_ID, UUID.randomUUID(), null));
    }

    @Test
    void testUnassignOwnProductFromArchivedCategory() {
        final var productId = UUID.randomUUID();
        when(categoryDao.loadById(CATEGORY_ID)).thenReturn(category(CategoryStatus.ARCHIVED));
        when(productDao.loadById(productId)).thenReturn(product(REQUESTER));

        service.unassignProduct(CATEGORY_ID, productId, REQUESTER);

        verify(assignmentDao).delete(CATEGORY_ID, productId);
    }

    @Test
    void testUnassignForeignProductIsRejected() {
        final var productId = UUID.randomUUID();
        when(categoryDao.loadById(CATEGORY_ID)).thenReturn(category(CategoryStatus.ACTIVE));
        when(productDao.loadById(productId)).thenReturn(product(UUID.randomUUID()));

        assertThrows(AuthorizationException.class, () -> service.unassignProduct(CATEGORY_ID, productId, REQUESTER));

        verify(assignmentDao, never()).delete(any(), any());
    }

    @Test
    void testUnassignMissingProductIsQuietNoOp() {
        final var productId = UUID.randomUUID();
        when(categoryDao.loadById(CATEGORY_ID)).thenReturn(category(CategoryStatus.ACTIVE));
        when(productDao.loadById(productId)).thenReturn(null);

        assertDoesNotThrow(() -> service.unassignProduct(CATEGORY_ID, productId, REQUESTER));

        verify(assignmentDao, never()).delete(any(), any());
    }

    private static Category category(CategoryStatus status) {
        return new Category(UUID.randomUUID(), Instant.now(), Instant.now(), status);
    }

    private static Product product(UUID creatorId) {
        final var product = new Product();
        product.setCreatedById(creatorId);
        return product;
    }

    private static List<CategoryAssignment> persisted(Collection<CategoryAssignment> transients) {
        return transients
                .stream()
                .map(a -> new CategoryAssignment(UUID.randomUUID(), Instant.now(), a.getCategoryId(), a.getProductId(), a.getAssignedById()))
                .toList();
    }

    private static ProductAssignmentInfo info(UUID creatorId, ProductStatus status) {
        return new ProductAssignmentInfo(creatorId, status);
    }
}
