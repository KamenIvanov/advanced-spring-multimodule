package com.pe.advanced.spring.config;

import com.pe.advanced.dao.api.category.CategoryDao;
import com.pe.advanced.dao.api.category.assignment.CategoryAssignmentDao;
import com.pe.advanced.dao.api.product.ProductDao;
import com.pe.advanced.dao.impl.category.CategoryDaoImpl;
import com.pe.advanced.dao.impl.category.CategoryRepository;
import com.pe.advanced.dao.impl.category.assignment.CategoryAssignmentDaoImpl;
import com.pe.advanced.dao.impl.category.assignment.CategoryAssignmentRepository;
import com.pe.advanced.dao.impl.product.ProductDaoImpl;
import com.pe.advanced.dao.impl.product.ProductRepository;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.validation.Validator;
import org.hibernate.cfg.BatchSettings;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.dialect.MySQLDialect;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.Properties;

@EnableJpaRepositories(
        basePackages = "com.pe.advanced.dao.impl",
        entityManagerFactoryRef = "entityManagerFactory"
)
@EntityScan(basePackages = "com.pe.advanced.dao.impl")
public class PersistenceConfiguration {

    @Bean
    @DependsOn("flyway")
    public AbstractEntityManagerFactoryBean entityManagerFactory(HikariDataSource dataSource) {
        final var properties = new Properties();
        properties.setProperty(JdbcSettings.DIALECT, MySQLDialect.class.getName());
        properties.setProperty(JdbcSettings.SHOW_SQL, "false");
        properties.setProperty(BatchSettings.STATEMENT_BATCH_SIZE, "50");

        final var em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setPackagesToScan("com.pe.advanced.dao.impl");
        em.setJpaProperties(properties);
        return em;
    }

    @Bean
    public ProductDao productDao(ProductRepository productRepository, Validator validator) {
        return new ProductDaoImpl(productRepository, validator);
    }

    @Bean
    public CategoryDao categoryDao(CategoryRepository categoryRepository, Validator validator) {
        return new CategoryDaoImpl(categoryRepository, validator);
    }

    @Bean
    public CategoryAssignmentDao categoryAssignmentDao(CategoryAssignmentRepository repository) {
        return new CategoryAssignmentDaoImpl(repository);
    }
}
