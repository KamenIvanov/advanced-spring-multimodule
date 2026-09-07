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

    ResultPage<Product> getProducts(int page, int size, ProductSort sort, SortDirection direction);

    void changeStatus(UUID id, ProductStatus newStatus, UUID requesterId);
}
