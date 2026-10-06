package com.pe.advanced.dao.impl.product;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends CrudRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {

    @Query("SELECT p FROM ProductEntity p WHERE p.sku = :sku")
    ProductEntity loadBySku(@Param("sku") String sku);

    @Query("SELECT p.id AS id, p.createdById AS createdById, p.status AS status FROM ProductEntity p WHERE p.id IN :ids")
    List<ProductAssignmentProjection> findAssignmentInfo(@Param("ids") Collection<UUID> ids);

    interface ProductAssignmentProjection {
        UUID getId();
        UUID getCreatedById();
        ProductStatusEntity getStatus();
    }
}
