package com.pe.advanced.bl.service;

import java.util.List;
import java.util.UUID;

public interface CategoryAssignmentsService {

    List<ProductAssignmentResult> assignProducts(UUID categoryId, List<UUID> productIds, UUID requesterId);

    void unassignProduct(UUID categoryId, UUID productId, UUID requesterId);

}