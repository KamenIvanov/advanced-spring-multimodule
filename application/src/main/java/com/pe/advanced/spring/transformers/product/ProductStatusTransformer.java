package com.pe.advanced.spring.transformers.product;

import com.pe.advanced.api.dto.product.ProductStatusDto;
import com.pe.advanced.domain.product.ProductStatus;
import com.pe.advanced.domain.transformers.AbstractEnumTransformer;

public class ProductStatusTransformer extends AbstractEnumTransformer<ProductStatus, ProductStatusDto> {

    public static final ProductStatusTransformer instance = new ProductStatusTransformer();

    private ProductStatusTransformer() {
       super(ProductStatus.class, ProductStatusDto.class);
    }
}
