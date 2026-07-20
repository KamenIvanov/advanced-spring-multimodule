package com.pe.advanced.dao.impl.category;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CategoryRepository extends CrudRepository<CategoryEntity, UUID>, JpaSpecificationExecutor<CategoryEntity> {

}
