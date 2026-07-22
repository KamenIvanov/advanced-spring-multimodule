package com.pe.advanced.dao.api.product;


import com.pe.advanced.dao.api.CrudDao;
import com.pe.advanced.dao.api.search.SearchableDao;
import com.pe.advanced.domain.product.Product;

import java.util.UUID;

public interface ProductDao extends CrudDao<UUID, Product>, SearchableDao<Product, ProductSearchParams> {

    Product loadBySku(String sku);
}
