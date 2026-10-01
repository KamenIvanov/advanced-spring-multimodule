package com.pe.advanced.bl.service.impl.category.assignments;

import com.pe.advanced.bl.service.CategoryAssignmentsService;
import com.pe.advanced.bl.service.ProductAssignmentResult;
import com.pe.advanced.bl.service.impl.AbstractService;
import com.pe.advanced.dao.api.category.CategoryDao;
import com.pe.advanced.dao.api.category.assignment.CategoryAssignmentDao;
import com.pe.advanced.dao.api.product.ProductAssignmentInfo;
import com.pe.advanced.dao.api.product.ProductDao;
import com.pe.advanced.domain.category.CategoryAssignment;
import com.pe.advanced.domain.category.CategoryStatus;
import com.pe.advanced.domain.exceptions.AuthorizationException;
import com.pe.advanced.domain.exceptions.ConflictException;
import com.pe.advanced.domain.product.ProductStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CategoryAssignmentsServiceImpl extends AbstractService implements CategoryAssignmentsService {

    private final CategoryAssignmentDao assignmentDao;
    private final CategoryDao categoryDao;
    private final ProductDao productDao;

    public CategoryAssignmentsServiceImpl(CategoryAssignmentDao assignmentDao, CategoryDao categoryDao, ProductDao productDao) {
        this.assignmentDao = assignmentDao;
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    @Override
    @Transactional
    public List<ProductAssignmentResult> assignProducts(UUID categoryId, List<UUID> productIds, UUID requesterId) {
        if (requesterId == null) {
            throw new AuthorizationException(UNAUTHORIZED);
        }
        Objects.requireNonNull(productIds, "productIds are mandatory");

        final var category = loadOrThrowNotFound(() -> categoryDao.loadById(categoryId));
        if (category.getStatus() == CategoryStatus.ARCHIVED) {
            throw new ConflictException("Category is archived and does not accept new products.");
        }

        // a repeated id is reported once, in the position of its first occurrence
        final Set<UUID> requested = new LinkedHashSet<>(productIds);
        final Map<UUID, ProductAssignmentInfo> products = productDao.findAssignmentInfo(requested);

        final Set<UUID> assignable = requested.stream()
                .filter(id -> isAssignable(products.get(id), requesterId))
                .collect(Collectors.toSet());
        final Set<UUID> alreadyAssigned = assignmentDao.findAssignedProductIds(categoryId, assignable);

        final var toAssign = requested.stream()
                .filter(assignable::contains)
                .filter(id -> !alreadyAssigned.contains(id))
                .map(id -> new CategoryAssignment(categoryId, id, requesterId))
                .toList();

        final Map<UUID, CategoryAssignment> saved = assignmentDao
                .saveAll(toAssign)
                .stream()
                .collect(Collectors.toMap(CategoryAssignment::getProductId, Function.identity()));

        return requested.stream()
                .map(id -> toResult(id, requesterId, products, saved))
                .toList();
    }

    @Override
    @Transactional
    public void unassignProduct(UUID categoryId, UUID productId, UUID requesterId) {
        if (requesterId == null) {
            throw new AuthorizationException(UNAUTHORIZED);
        }
        // the category must exist but may be archived: removal is always allowed
        loadOrThrowNotFound(() -> categoryDao.loadById(categoryId));

        final var product = productDao.loadById(productId);
        if (product == null) {
            return;
        }
        if (!requesterId.equals(product.getCreatedById())) {
            throw new AuthorizationException(UNAUTHORIZED);
        }
        assignmentDao.delete(categoryId, productId);
    }

    private static boolean isAssignable(ProductAssignmentInfo info, UUID requesterId) {
        return info != null
                && requesterId.equals(info.createdById())
                && info.status() != ProductStatus.ARCHIVED;
    }

    private ProductAssignmentResult toResult(UUID productId, UUID requesterId, Map<UUID, ProductAssignmentInfo> products, Map<UUID, CategoryAssignment> saved) {
        final var info = products.get(productId);
        if (info == null) {
            return new ProductAssignmentResult.ProductNotFound(productId);
        }
        if (!requesterId.equals(info.createdById())) {
            return new ProductAssignmentResult.ProductNotOwned(productId);
        }
        if (info.status() == ProductStatus.ARCHIVED) {
            return new ProductAssignmentResult.ProductArchived(productId);
        }
        final var assignment = saved.get(productId);
        return assignment != null
                ? new ProductAssignmentResult.Assigned(productId, assignment)
                : new ProductAssignmentResult.AlreadyAssigned(productId);
    }
}
