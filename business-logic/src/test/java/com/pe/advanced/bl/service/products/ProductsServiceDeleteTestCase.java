package com.pe.advanced.bl.service.products;

import com.pe.advanced.bl.service.impl.product.ProductsServiceImpl;
import com.pe.advanced.dao.api.category.assignment.CategoryAssignmentDao;
import com.pe.advanced.dao.api.product.ProductDao;
import com.pe.advanced.domain.exceptions.AuthorizationException;
import com.pe.advanced.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductsServiceDeleteTestCase {

    private static final UUID OWNER = UUID.randomUUID();

    @Mock
    private ProductDao dao;
    @Mock
    private CategoryAssignmentDao assignmentDao;

    private ProductsServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProductsServiceImpl(dao, assignmentDao);
    }

    @Test
    void testDeleteAlsoRemovesAssignments() {
        final var id = UUID.randomUUID();
        final var product = product(OWNER);
        when(dao.loadById(id)).thenReturn(product);

        service.delete(id, OWNER);

        verify(dao).delete(product);
        verify(assignmentDao).deleteAllForProduct(id);
    }

    @Test
    void testUnauthorizedDeleteLeavesAssignmentsAlone() {
        final var id = UUID.randomUUID();
        when(dao.loadById(id)).thenReturn(product(UUID.randomUUID()));

        assertThrows(AuthorizationException.class, () -> service.delete(id, OWNER));

        verify(dao, never()).delete(any());
        verifyNoInteractions(assignmentDao);
    }

    @Test
    void testMissingProductStillRunsTheCleanup() {
        final var id = UUID.randomUUID();
        when(dao.loadById(id)).thenReturn(null);

        service.delete(id, OWNER);

        verify(dao, never()).delete(any());
        verify(assignmentDao).deleteAllForProduct(id);
    }

    private static Product product(UUID creatorId) {
        final var product = new Product();
        product.setCreatedById(creatorId);
        return product;
    }
}
