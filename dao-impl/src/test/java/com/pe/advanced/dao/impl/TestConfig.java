package com.pe.advanced.dao.impl;

import com.pe.advanced.dao.api.category.CategoryDao;
import com.pe.advanced.dao.api.product.ProductDao;
import com.pe.advanced.dao.impl.category.CategoryDaoImpl;
import com.pe.advanced.dao.impl.product.ProductDaoImpl;
import com.pe.advanced.dao.impl.proxy.CategoryDaoProxy;
import com.pe.advanced.dao.impl.proxy.ProductsDaoProxy;
import jakarta.persistence.EntityManager;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;

public class TestConfig {

    @Bean
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Bean
    public ProductDao productDao(ProductDaoImpl productDao, EntityManager entityManager) {
        return new ProductsDaoProxy(productDao, entityManager);
    }

    @Bean
    public CategoryDao categoryDao(CategoryDaoImpl categoryDao, EntityManager entityManager) {
        return new CategoryDaoProxy(categoryDao, entityManager);
    }
}
