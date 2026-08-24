package com.pe.advanced.dao.impl.proxy;

import com.pe.advanced.dao.api.category.CategoryDao;
import com.pe.advanced.dao.api.category.CategorySearchParams;
import com.pe.advanced.domain.category.Category;
import jakarta.persistence.EntityManager;

import java.util.UUID;

public class CategoryDaoProxy extends SearchableCrudDaoProxy<UUID, Category, CategorySearchParams, CategoryDao> implements CategoryDao {

    public CategoryDaoProxy(CategoryDao proxied, EntityManager entityManager) {
        super(proxied, entityManager);
    }

}
