package com.pe.advanced.bl.service;

import com.pe.advanced.dao.api.product.ProductSort;
import com.pe.advanced.dao.api.search.ResultPage;
import com.pe.advanced.dao.api.search.SortDirection;
import com.pe.advanced.domain.product.NewProduct;
import com.pe.advanced.domain.product.Product;
import com.pe.advanced.domain.product.ProductStatus;
import com.pe.advanced.domain.product.UpdateProduct;

import java.util.UUID;

public interface ProductsService extends CrudService<NewProduct, UpdateProduct, Product> {

    /**
     * The catalogue as anybody may read it. No requester, and therefore no ownership filter.
     * <p>
     * The criteria arrive as values rather than as the search layer's parameter object, so that
     * nothing above this interface can express a filter this interface did not offer.
     *
     * @param query text contained in the product's name, or null to match every name
     */
    ResultPage<Product> getProducts(int page, int size, ProductSort sort, SortDirection direction, String query);

    /**
     * The same search, narrowed to what the requester created. The ownership filter is built
     * here rather than passed in, so that no caller can widen it.
     */
    ResultPage<Product> getOwnProducts(int page, int size, ProductSort sort, SortDirection direction, String query, UUID requesterId);

    /**
     * One product for the catalogue. Unlike {@link #loadById(UUID, UUID)} this performs no
     * ownership check, because the reader is anonymous and the projection carries nothing
     * that is not already public.
     */
    Product loadPublicById(UUID id);

    /**
     * Moves the product into a new status and returns it as it now stands. An unchanged status
     * is not an error and writes nothing, so the product comes back as it was found.
     */
    Product changeStatus(UUID id, ProductStatus newStatus, UUID requesterId);
}
