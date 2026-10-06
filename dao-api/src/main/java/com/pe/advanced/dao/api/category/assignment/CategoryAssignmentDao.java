package com.pe.advanced.dao.api.category.assignment;

import com.pe.advanced.domain.category.CategoryAssignment;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface CategoryAssignmentDao {

    List<CategoryAssignment> saveAll(Collection<CategoryAssignment> assignments);

    Set<UUID> findAssignedProductIds(UUID categoryId, Collection<UUID> productIds);

    void delete(UUID categoryId, UUID productId);

    void deleteAllForCategory(UUID categoryId);

    void deleteAllForProduct(UUID productId);
}
