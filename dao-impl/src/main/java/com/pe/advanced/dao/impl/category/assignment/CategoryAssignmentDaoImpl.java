package com.pe.advanced.dao.impl.category.assignment;

import com.pe.advanced.dao.api.category.assignment.CategoryAssignmentDao;
import com.pe.advanced.dao.impl.transformers.category.CategoryAssignmentTransformer;
import com.pe.advanced.domain.category.CategoryAssignment;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class CategoryAssignmentDaoImpl implements CategoryAssignmentDao {

    private final CategoryAssignmentRepository repository;

    public CategoryAssignmentDaoImpl(CategoryAssignmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CategoryAssignment> saveAll(Collection<CategoryAssignment> assignments) {
        Objects.requireNonNull(assignments, "Assignments are mandatory");
        if (assignments.isEmpty()) {
            return List.of();
        }
        // the column is timestamp(3): truncate so a returned instance equals a reloaded one
        final var now = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        final var entities = new ArrayList<CategoryAssignmentEntity>(assignments.size());
        for (final var assignment : assignments) {
            if (assignment.getId() != null) {
                throw new IllegalArgumentException("Assignment existing!");
            }
            final var entity = CategoryAssignmentTransformer.instance.createInput(assignment);
            entity.setCreatedAt(now);
            entities.add(entity);
        }
        return CategoryAssignmentTransformer.instance.createOutput(repository.saveAllAndFlush(entities));
    }

    @Override
    public Set<UUID> findAssignedProductIds(UUID categoryId, Collection<UUID> productIds) {
        if (productIds.isEmpty()) {
            return Set.of();
        }
        return repository.findAssignedProductIds(categoryId, productIds);
    }

    @Override
    public void delete(UUID categoryId, UUID productId) {
        repository.deleteAssignment(categoryId, productId);
    }

    @Override
    public void deleteAllForCategory(UUID categoryId) {
        repository.deleteAllForCategory(categoryId);
    }

    @Override
    public void deleteAllForProduct(UUID productId) {
        repository.deleteAllForProduct(productId);
    }
}
