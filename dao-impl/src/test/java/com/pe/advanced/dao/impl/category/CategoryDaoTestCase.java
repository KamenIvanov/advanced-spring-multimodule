package com.pe.advanced.dao.impl.category;

import com.pe.advanced.dao.api.category.CategoryDao;
import com.pe.advanced.dao.api.category.CategorySearchParams;
import com.pe.advanced.dao.api.category.CategorySort;
import com.pe.advanced.dao.api.search.SortDirection;
import com.pe.advanced.dao.impl.AbstractSearchableTestCase;
import com.pe.advanced.domain.asserter.DeepEqualsAsserter;
import com.pe.advanced.domain.asserter.category.CategoryAsserter;
import com.pe.advanced.domain.category.Category;
import com.pe.advanced.domain.category.CategoryStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryDaoTestCase extends AbstractSearchableTestCase<UUID, Category, CategoryDao, CategorySearchParams> {

    @Autowired
    private CategoryDao categoryDao;

    @ParameterizedTest
    @EnumSource(CategoryStatus.class)
    void testSearchByStatus(CategoryStatus status) {
        for (final var each : CategoryStatus.values()) {
            saved("Category " + each, each);
        }
        final var params = searchAll();
        params.setStatus(status);

        final var result = categoryDao.search(params);

        assertEquals(1, result.elements().size());
        assertEquals(status, result.elements().getFirst().getStatus());
    }

    @Test
    void testSearchByNameIsCaseInsensitiveAndPartial() {
        saved("Winter Jackets", CategoryStatus.INACTIVE);
        saved("Summer Shoes", CategoryStatus.INACTIVE);
        final var params = searchAll();
        params.setName("jACK");

        final var result = categoryDao.search(params);

        assertEquals(1, result.elements().size());
        assertEquals("Winter Jackets", result.elements().getFirst().getName());
    }

    @ParameterizedTest
    @EnumSource(CategorySort.class)
    void testEverySortKeyIsAValidProperty(CategorySort sort) {
        saved("Alpha", CategoryStatus.INACTIVE);
        saved("Bravo", CategoryStatus.ACTIVE);
        final var params = searchAll();
        params.setSortBy(sort);
        params.setSortDirection(SortDirection.ASC);

        assertEquals(2, categoryDao.search(params).elements().size());
    }

    @Test
    void testSortByNameAscending() {
        saved("Charlie", CategoryStatus.INACTIVE);
        saved("Alpha", CategoryStatus.INACTIVE);
        saved("Bravo", CategoryStatus.INACTIVE);
        final var params = searchAll();
        params.setSortBy(CategorySort.NAME);
        params.setSortDirection(SortDirection.ASC);

        final var names = categoryDao.search(params).elements().stream().map(Category::getName).toList();

        assertEquals(List.of("Alpha", "Bravo", "Charlie"), names);
    }

    @Test
    void testSortWithoutDirection() {
        saved("Alpha", CategoryStatus.INACTIVE);
        final var params = searchAll();
        params.setSortBy(CategorySort.NAME);

        assertDoesNotThrow(() -> categoryDao.search(params));
    }

    @Test
    void testSortWithExplicitNullDirection() {
        saved("Alpha", CategoryStatus.INACTIVE);
        final var params = searchAll();
        params.setSortBy(CategorySort.NAME);
        params.setSortDirection(null);

        assertDoesNotThrow(() -> categoryDao.search(params));
    }

    @Test
    void testSortWithoutDirectionDefaultsToDescending() {
        saved("Alpha", CategoryStatus.INACTIVE);
        saved("Charlie", CategoryStatus.INACTIVE);
        saved("Bravo", CategoryStatus.INACTIVE);
        final var params = searchAll();
        params.setSortDirection(null);
        params.setSortBy(CategorySort.NAME);

        final var names = categoryDao.search(params).elements().stream().map(Category::getName).toList();

        assertEquals(List.of("Charlie", "Bravo", "Alpha"), names);
    }

    @Override
    protected CategorySearchParams createSearchParams() {
        return new CategorySearchParams();
    }

    @Override
    protected CategoryDao getDao() {
        return categoryDao;
    }

    @Override
    public Category createDomain() {
        final var category = new Category();
        category.setCreatedById(UUID.randomUUID());
        category.setUpdatedById(UUID.randomUUID());
        category.setName("Simple Category name");
        return category;
    }

    @Override
    protected DeepEqualsAsserter<Category> getAsserter() {
        return CategoryAsserter.instance;
    }

    @Override
    protected Category updateDomain(Category domain) {
        domain.setName("New name");
        return domain;
    }

    private void saved(String name, CategoryStatus status) {
        final var category = createDomain();
        category.setName(name);
        if (status != CategoryStatus.INACTIVE) {
            category.transitionTo(status); // INACTIVE is the default, ACTIVE and ARCHIVED are both reachable from it
        }
        runSave(category);
    }

    private CategorySearchParams searchAll() {
        final var params = createSearchParams();
        params.setPage(0);
        params.setSize(10);
        return params;
    }
}
