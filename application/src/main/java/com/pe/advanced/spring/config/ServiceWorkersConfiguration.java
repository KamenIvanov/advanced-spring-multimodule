package com.pe.advanced.spring.config;

import com.pe.advanced.bl.service.CategoriesService;
import com.pe.advanced.bl.service.CategoryAssignmentsService;
import com.pe.advanced.bl.service.ProductsService;
import com.pe.advanced.bl.service.impl.category.CategoriesServiceImpl;
import com.pe.advanced.bl.service.impl.category.assignments.CategoryAssignmentsServiceImpl;
import com.pe.advanced.bl.service.impl.product.ProductsServiceImpl;
import com.pe.advanced.dao.api.category.CategoryDao;
import com.pe.advanced.dao.api.category.assignment.CategoryAssignmentDao;
import com.pe.advanced.dao.api.product.ProductDao;
import org.springframework.context.annotation.Bean;

public class ServiceWorkersConfiguration {

    @Bean
    public ProductsService productsService(ProductDao productDao, CategoryAssignmentDao assignmentDao) {
        return new ProductsServiceImpl(productDao, assignmentDao);
    }

    @Bean
    public CategoriesService categoriesService(CategoryDao categoryDao, CategoryAssignmentDao assignmentDao) {
        return new CategoriesServiceImpl(categoryDao, assignmentDao);
    }

    @Bean
    public CategoryAssignmentsService categoryAssignmentsService(
            CategoryAssignmentDao assignmentDao,
            CategoryDao categoryDao,
            ProductDao productDao
    ) {
        return new CategoryAssignmentsServiceImpl(assignmentDao, categoryDao, productDao);
    }
}
