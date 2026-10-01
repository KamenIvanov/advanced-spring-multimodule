package com.pe.advanced.bl.service;

import com.pe.advanced.domain.category.CategoryAssignment;

import java.util.UUID;

public sealed interface ProductAssignmentResult {

    UUID productId();

    record Assigned(UUID productId, CategoryAssignment assignment) implements ProductAssignmentResult { }

    record AlreadyAssigned(UUID productId) implements ProductAssignmentResult { }

    record ProductNotFound(UUID productId) implements ProductAssignmentResult { }

    record ProductNotOwned(UUID productId) implements ProductAssignmentResult { }

    record ProductArchived(UUID productId) implements ProductAssignmentResult { }
}
