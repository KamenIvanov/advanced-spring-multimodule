package com.pe.advanced.dao.impl.transformers.product;

import com.pe.advanced.dao.impl.product.ProductStatusEntity;
import com.pe.advanced.domain.product.ProductStatus;
import com.pe.advanced.domain.transformers.AbstractEnumTransformer;

public class ProductTypeTransformer extends AbstractEnumTransformer<ProductStatusEntity, ProductStatus> {

    public static final ProductTypeTransformer instance = new ProductTypeTransformer();

    private ProductTypeTransformer() {
        super(ProductStatusEntity.class, ProductStatus.class);
    }
}
