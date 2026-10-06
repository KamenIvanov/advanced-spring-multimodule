package com.pe.advanced.bl.service.categories;

import com.pe.advanced.bl.service.impl.category.CategoriesServiceImpl;
import com.pe.advanced.dao.api.category.CategoryDao;
import com.pe.advanced.dao.api.category.assignment.CategoryAssignmentDao;
import com.pe.advanced.domain.category.Category;
import com.pe.advanced.domain.category.CategoryStatus;
import com.pe.advanced.domain.exceptions.AuthorizationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriesServiceDeleteTestCase {

    private static final UUID OWNER = UUID.randomUUID();

    @Mock
    private CategoryDao dao;
    @Mock
    private CategoryAssignmentDao assignmentDao;

    private CategoriesServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CategoriesServiceImpl(dao, assignmentDao);
    }

    @Test
    void testDeleteAlsoRemovesAssignments() {
        final var id = UUID.randomUUID();
        final var category = category(OWNER);
        when(dao.loadById(id)).thenReturn(category);

        service.delete(id, OWNER);

        verify(dao).delete(category);
        verify(assignmentDao).deleteAllForCategory(id);
    }

    @Test
    void testUnauthorizedDeleteLeavesAssignmentsAlone() {
        final var id = UUID.randomUUID();
        when(dao.loadById(id)).thenReturn(category(UUID.randomUUID()));

        assertThrows(AuthorizationException.class, () -> service.delete(id, OWNER));

        verify(dao, never()).delete(any());
        verifyNoInteractions(assignmentDao);
    }

    @Test
    void testMissingCategoryStillRunsTheCleanup() {
        final var id = UUID.randomUUID();
        when(dao.loadById(id)).thenReturn(null);

        service.delete(id, OWNER);

        verify(dao, never()).delete(any());
        verify(assignmentDao).deleteAllForCategory(id);
    }

    private static Category category(UUID creatorId) {
        final var category = new Category(UUID.randomUUID(), Instant.now(), Instant.now(), CategoryStatus.ACTIVE);
        category.setCreatedById(creatorId);
        return category;
    }
}