package com.pe.advanced.dao.api.product;


import com.pe.advanced.dao.api.SearchableDao;
import com.pe.advanced.domain.product.Product;

import java.util.UUID;

public interface ProductDao extends SearchableDao<UUID, Product, ProductSearchQuery> {

    Product loadBySku(String sku);
}
