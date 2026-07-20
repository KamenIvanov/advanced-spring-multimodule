package com.pe.advanced.dao.impl.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.function.Predicate;

public class TablesEraser {

    public static void emptyAllTables(EntityManagerFactory emf, DbQueryFactory queryFactory) {
        emptyAllTables(emf, queryFactory, s -> true);
    }

    public static void emptyAllTables(EntityManagerFactory emf, DbQueryFactory queryFactory, Predicate<String> filter) {
        final EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Disable foreign keys
            em.createNativeQuery(queryFactory.disableForeignKeys()).executeUpdate();

            // Get all tables
            final List<String> allTableNames = em.createNativeQuery(queryFactory.selectAllTableNames()).getResultList();

            // Truncate each table
            for (String tableName : allTableNames) {
                if (filter.test(tableName)) {
                    em.createNativeQuery("DELETE FROM " + tableName).executeUpdate();
                }
            }

            // Enable foreign keys
            em.createNativeQuery(queryFactory.enableForeignKeys()).executeUpdate();
            em.getTransaction().commit();
        } catch (Throwable th) {
            em.getTransaction().rollback();
            throw new RuntimeException(th);
        } finally {
            em.close();
        }
    }
}
