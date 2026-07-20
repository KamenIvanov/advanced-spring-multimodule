package com.pe.advanced.dao.impl.product;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ProductRepository extends CrudRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {

    @Query("SELECT p FROM ProductEntity p WHERE p.sku = :sku")
    ProductEntity loadBySku(@Param("sku") String sku);
}
