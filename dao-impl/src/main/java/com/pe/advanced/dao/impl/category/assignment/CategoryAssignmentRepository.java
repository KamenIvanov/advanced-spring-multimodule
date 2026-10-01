package com.pe.advanced.dao.impl.category.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface CategoryAssignmentRepository extends JpaRepository<CategoryAssignmentEntity, UUID> {

    @Query("""
            SELECT a.productId FROM CategoryAssignmentEntity a
            WHERE a.categoryId = :categoryId AND a.productId IN :productIds
            """)
    Set<UUID> findAssignedProductIds(@Param("categoryId") UUID categoryId, @Param("productIds") Collection<UUID> productIds);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM CategoryAssignmentEntity a WHERE a.categoryId = :categoryId AND a.productId = :productId")
    void deleteAssignment(@Param("categoryId") UUID categoryId, @Param("productId") UUID productId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM CategoryAssignmentEntity a WHERE a.categoryId = :categoryId")
    void deleteAllForCategory(@Param("categoryId") UUID categoryId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM CategoryAssignmentEntity a WHERE a.productId = :productId")
    void deleteAllForProduct(@Param("productId") UUID productId);
}
