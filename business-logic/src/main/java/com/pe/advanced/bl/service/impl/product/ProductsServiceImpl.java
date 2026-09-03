package com.pe.advanced.bl.service.impl.product;

import com.pe.advanced.bl.service.ProductsService;
import com.pe.advanced.bl.service.impl.AbstractCrudService;
import com.pe.advanced.bl.transformer.product.NewProductTransformer;
import com.pe.advanced.bl.transformer.product.UpdateProductTransformer;
import com.pe.advanced.dao.api.product.ProductDao;
import com.pe.advanced.dao.api.product.ProductSearchParams;
import com.pe.advanced.dao.api.product.ProductSort;
import com.pe.advanced.dao.api.search.ResultPage;
import com.pe.advanced.dao.api.search.SortDirection;
import com.pe.advanced.domain.exceptions.AuthorizationException;
import com.pe.advanced.domain.product.NewProduct;
import com.pe.advanced.domain.product.Product;
import com.pe.advanced.domain.product.ProductStatus;
import com.pe.advanced.domain.product.UpdateProduct;
import com.pe.advanced.domain.transformers.Transformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class ProductsServiceImpl extends AbstractCrudService<NewProduct, UpdateProduct, Product, ProductDao> implements ProductsService {

    public ProductsServiceImpl(ProductDao dao) {
        super(dao);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultPage<Product> getProducts(int page, int size, ProductSort sort, SortDirection direction) {
        final var query = new ProductSearchParams();
        query.setPage(page);
        query.setSize(size);
        query.setSortBy(sort);
        query.setSortDirection(direction);

        return getDao().search(query);
    }

    @Override
    public void changeStatus(UUID id, ProductStatus newStatus, UUID requesterId) {
        if (requesterId == null) {
            throw new AuthorizationException(UNAUTHORIZED);
        }

        final var product = loadOrThrowNotFound(() -> getDao().loadById(id));

        // Can the requester modify the entity?
        authorize(product, requesterId);

        product.transitionTo(newStatus);
        product.setUpdatedById(requesterId);

        getDao().update(product);
    }

    @Override
    protected void preProcessNewEntity(Product product, UUID requesterId) {
        product.setCreatedById(requesterId);
        product.setUpdatedById(requesterId);
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
}
