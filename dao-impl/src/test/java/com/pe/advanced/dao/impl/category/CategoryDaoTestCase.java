package com.pe.advanced.dao.impl.category;

import com.pe.advanced.dao.api.category.CategoryDao;
import com.pe.advanced.dao.api.category.CategorySearchParams;
import com.pe.advanced.dao.impl.AbstractSearchableTestCase;
import com.pe.advanced.domain.asserter.DeepEqualsAsserter;
import com.pe.advanced.domain.asserter.category.CategoryAsserter;
import com.pe.advanced.domain.category.Category;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.UUID;

class CategoryDaoTestCase extends AbstractSearchableTestCase<UUID, Category, CategoryDao, CategorySearchParams> {

    @Autowired
    private CategoryDao categoryDao;

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
        category.setCreatedAt(Instant.now());
        category.setUpdatedAt(Instant.now());
        category.setCreatedById(UUID.randomUUID());
        category.setUpdatedById(UUID.randomUUID());
        category.setName("Simple Category name");
        category.setActive(true);
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
}
