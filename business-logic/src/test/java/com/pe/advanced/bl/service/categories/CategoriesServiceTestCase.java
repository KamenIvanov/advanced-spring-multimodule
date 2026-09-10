package com.pe.advanced.bl.service.categories;

import com.pe.advanced.bl.service.AbstractCrudServiceTestCase;
import com.pe.advanced.bl.service.CategoriesService;
import com.pe.advanced.bl.service.impl.category.CategoriesServiceImpl;
import com.pe.advanced.dao.api.category.CategoryDao;
import com.pe.advanced.domain.category.Category;
import com.pe.advanced.domain.category.CategoryStatus;
import com.pe.advanced.domain.category.NewCategory;
import com.pe.advanced.domain.category.UpdateCategory;
import com.pe.advanced.domain.exceptions.AuthorizationException;
import com.pe.advanced.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriesServiceTestCase extends AbstractCrudServiceTestCase<Category, NewCategory, UpdateCategory, CategoryDao, CategoriesService> {

    @Mock
    private CategoryDao categoryDao;

    @InjectMocks
    private CategoriesServiceImpl categoriesService;

    @Test
    void create_ShouldMapNameThroughNewCategoryTransformer() {
        final NewCategory newDto = createValidNewDomain();
        when(categoryDao.create(any())).thenAnswer(invocation -> withId(invocation.getArgument(0), UUID.randomUUID()));

        final Category result = categoriesService.create(newDto, requesterId);
        assertEquals(newDto.getName(), result.getName());
        assertEquals(CategoryStatus.INACTIVE, result.getStatus());
    }

    @Test
    void update_ShouldMapNameThroughUpdateCategoryTransformer() {
        final Category existing = persistedCategory(requesterId, CategoryStatus.INACTIVE);
        final UpdateCategory updateDto = createUpdateDomain();

        when(categoryDao.loadById(existing.getId())).thenReturn(existing);
        when(categoryDao.update(existing)).thenReturn(existing);

        final Category result = categoriesService.update(existing.getId(), updateDto, requesterId);
        assertEquals(updateDto.getName(), result.getName());
    }

    @Test
    void changeStatus_WhenRequesterIdIsNull_ShouldThrowAuthorizationException() {
        assertThrows(AuthorizationException.class, () -> categoriesService.changeStatus(UUID.randomUUID(), CategoryStatus.ACTIVE, null));
        verifyNoInteractions(categoryDao);
    }

    @Test
    void changeStatus_WhenEntityDoesNotExist_ShouldThrowNotFoundException() {
        final UUID missingId = UUID.randomUUID();
        when(categoryDao.loadById(missingId)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> categoriesService.changeStatus(missingId, CategoryStatus.ACTIVE, requesterId));
        verify(categoryDao, never()).update(any());
    }

    @Test
    void changeStatus_WhenRequesterIsNotOwner_ShouldThrowAuthorizationExceptionAndNeverPersist() {
        final Category existing = persistedCategory(alternativeRequesterId, CategoryStatus.INACTIVE);
        when(categoryDao.loadById(existing.getId())).thenReturn(existing);
        assertThrows(AuthorizationException.class, () -> categoriesService.changeStatus(existing.getId(), CategoryStatus.ACTIVE, requesterId));
        verify(categoryDao, never()).update(any());
    }

    @Test
    void changeStatus_WhenTransitionIsValid_ShouldPersistNewStatus() {
        final Category existing = persistedCategory(requesterId, CategoryStatus.INACTIVE);
        when(categoryDao.loadById(existing.getId())).thenReturn(existing);
        when(categoryDao.update(existing)).thenReturn(existing);

        categoriesService.changeStatus(existing.getId(), CategoryStatus.ACTIVE, requesterId);

        assertEquals(CategoryStatus.ACTIVE, existing.getStatus());
        verify(categoryDao, times(1)).update(existing);
    }

    @Test
    void changeStatus_WhenTransitionIsIllegal_ShouldThrowAndNeverPersist() {
        final Category existing = persistedCategory(requesterId, CategoryStatus.ACTIVE);
        when(categoryDao.loadById(existing.getId())).thenReturn(existing);
        assertThrows(IllegalStateException.class, () -> categoriesService.changeStatus(existing.getId(), CategoryStatus.INACTIVE, requesterId));
        verify(categoryDao, never()).update(any());
    }

    @Override
    protected CategoriesService getService() {
        return categoriesService;
    }

    @Override
    protected CategoryDao getMockDao() {
        return categoryDao;
    }

    @Override
    protected NewCategory createValidNewDomain() {
        final var dto = new NewCategory();
        dto.setName("Building Sets");
        return dto;
    }

    @Override
    protected UpdateCategory createUpdateDomain() {
        final var dto = new UpdateCategory();
        dto.setName("Construction Sets");
        return dto;
    }

    @Override
    protected Category withId(Category transientCategory, UUID id) {
        final var persisted = new Category(
                id,
                transientCategory.getCreatedAt(),
                transientCategory.getUpdatedAt(),
                transientCategory.getStatus()
        );
        persisted.setName(transientCategory.getName());
        persisted.setCreatedById(transientCategory.getCreatedById());
        persisted.setUpdatedById(transientCategory.getUpdatedById());
        return persisted;
    }

    private Category persistedCategory(UUID ownerId, CategoryStatus status) {
        final var category = new Category(UUID.randomUUID(), Instant.now(), Instant.now(), status);
        category.setName("Existing Category");
        category.setCreatedById(ownerId);
        category.setUpdatedById(ownerId);
        return category;
    }
}