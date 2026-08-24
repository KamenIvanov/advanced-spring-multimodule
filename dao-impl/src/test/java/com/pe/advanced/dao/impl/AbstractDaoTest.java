package com.pe.advanced.dao.impl;

import com.pe.advanced.dao.impl.category.CategoryDaoImpl;
import com.pe.advanced.dao.impl.hibernate.H2QueryFactory;
import com.pe.advanced.dao.impl.hibernate.TablesEraser;
import com.pe.advanced.dao.impl.product.ProductDaoImpl;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = TestApplication.class)
@Import({
        ProductDaoImpl.class,
        CategoryDaoImpl.class,
        TestConfig.class
})
public abstract class AbstractDaoTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @BeforeEach
    void setUp() {
        TablesEraser.emptyAllTables(entityManagerFactory, H2QueryFactory.instance);
    }
}
