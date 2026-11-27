package com.pe.advanced.bl.service.impl.product;

import com.pe.advanced.bl.service.ProductsService;
import com.pe.advanced.bl.service.impl.AbstractCrudService;
import com.pe.advanced.bl.transformer.product.NewProductTransformer;
import com.pe.advanced.bl.transformer.product.UpdateProductTransformer;
import com.pe.advanced.dao.api.category.assignment.CategoryAssignmentDao;
import com.pe.advanced.dao.api.product.ProductDao;
import com.pe.advanced.dao.api.product.ProductSearchParams;
import com.pe.advanced.dao.api.product.ProductSort;
import com.pe.advanced.dao.api.search.ResultPage;
import com.pe.advanced.dao.api.search.SortDirection;
import com.pe.advanced.domain.exceptions.AuthenticationException;
import com.pe.advanced.domain.exceptions.AuthorizationException;
import com.pe.advanced.domain.exceptions.NotFoundException;
import com.pe.advanced.domain.product.NewProduct;
import com.pe.advanced.domain.product.Product;
import com.pe.advanced.domain.product.ProductStatus;
import com.pe.advanced.domain.product.UpdateProduct;
import com.pe.advanced.domain.transformers.Transformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public class ProductsServiceImpl extends AbstractCrudService<NewProduct, UpdateProduct, Product, ProductDao> implements ProductsService {

    /**
     * Everything an anonymous reader may see. A draft is excluded because it has never been
     * published; an archived product is not, because it may still be linked to from somewhere and
     * the status field is what tells the reader it cannot be ordered.
     */
    private static final Set<ProductStatus> PUBLISHED = EnumSet.complementOf(EnumSet.of(ProductStatus.DRAFT));

    private final CategoryAssignmentDao assignmentDao;

    public ProductsServiceImpl(ProductDao dao, CategoryAssignmentDao assignmentDao) {
        super(dao);
        this.assignmentDao = assignmentDao;
    }

    @Override
    @Transactional
    public void delete(UUID id, UUID requesterId) {
        super.delete(id, requesterId);
        assignmentDao.deleteAllForProduct(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultPage<Product> getProducts(int page, int size, ProductSort sort, SortDirection direction, String query) {
        final var params = params(page, size, sort, direction, query);
        params.setStatuses(PUBLISHED);
        return getDao().search(params);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultPage<Product> getOwnProducts(int page, int size, ProductSort sort, SortDirection direction, String query, UUID requesterId) {
        if (requesterId == null) {
            throw new AuthenticationException(UNAUTHENTICATED);
        }

        final var params = params(page, size, sort, direction, query);
        params.setCreatedById(requesterId);
        return getDao().search(params);
    }

    @Override
    @Transactional(readOnly = true)
    public Product loadPublicById(UUID id) {
        final var product = loadOrThrowNotFound(() -> getDao().loadById(id));

        // A draft has never been published, so to an anonymous reader it does not exist. Saying
        // "forbidden" instead would confirm that the identification is a real product.
        if (product.getStatus() == ProductStatus.DRAFT) {
            throw new NotFoundException(MISSING_ENTITY);
        }

        return product;
    }

    @Override
    @Transactional
    public Product changeStatus(UUID id, ProductStatus newStatus, UUID requesterId, Integer expectedVersion) {
        if (requesterId == null) {
            throw new AuthenticationException(UNAUTHENTICATED);
        }

        final var product = loadOrThrowNotFound(() -> getDao().loadById(id));

        // Can the requester modify the entity?
        authorize(product, requesterId);

        if (product.getStatus() == newStatus) {
            return product;
        }

        product.transitionTo(newStatus);
        product.setUpdatedById(requesterId);

        return getDao().update(product, expectedVersion);
    }

    @Override
    protected Transformer<NewProduct, Product> getCreateTransformer() {
        return NewProductTransformer.instance;
    }

    @Override
    protected Transformer<UpdateProduct, Product> getUpdateTransformer() {
        return UpdateProductTransformer.instance;
    }

    @Override
    protected void authorize(Product domain, UUID requesterId) {
        if (!domain.getCreatedById().equals(requesterId)) {
            throw new AuthorizationException(UNAUTHORIZED);
        }
    }

    /**
     * An absent sort field or direction keeps whatever the parameter object defaults to, rather
     * than overwriting it with null.
     */
    private static ProductSearchParams params(int page, int size, ProductSort sort, SortDirection direction, String query) {
        final var params = new ProductSearchParams();
        params.setPage(page);
        params.setSize(size);
        params.setName(query);

        if (sort != null) {
            params.setSortBy(sort);
        }
        if (direction != null) {
            params.setSortDirection(direction);
        }

        return params;
    }
}
