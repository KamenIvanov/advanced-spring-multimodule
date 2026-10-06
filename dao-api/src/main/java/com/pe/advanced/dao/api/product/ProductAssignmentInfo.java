package com.pe.advanced.dao.api.product;

import com.pe.advanced.domain.product.ProductStatus;

import java.util.UUID;

public record ProductAssignmentInfo(UUID createdById, ProductStatus status) {
}