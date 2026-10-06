package com.pe.advanced.dao.impl.transformers.product;

import com.pe.advanced.dao.impl.product.ProductStatusEntity;
import com.pe.advanced.domain.product.ProductStatus;
import com.pe.advanced.domain.transformers.AbstractEnumTransformer;

public class ProductStatusTransformer extends AbstractEnumTransformer<ProductStatusEntity, ProductStatus> {

    public static final ProductStatusTransformer instance = new ProductStatusTransformer();

    private ProductStatusTransformer() {
        super(ProductStatusEntity.class, ProductStatus.class);
    }
}
