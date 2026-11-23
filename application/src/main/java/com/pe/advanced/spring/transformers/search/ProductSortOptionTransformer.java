package com.pe.advanced.spring.transformers.search;

import com.pe.advanced.api.dto.product.ProductSortOptionDto;
import com.pe.advanced.dao.api.product.ProductSort;
import com.pe.advanced.domain.transformers.AbstractEnumTransformer;

public class ProductSortOptionTransformer extends AbstractEnumTransformer<ProductSort, ProductSortOptionDto> {

    public static final ProductSortOptionTransformer instance = new ProductSortOptionTransformer();

    private ProductSortOptionTransformer() {
        super(ProductSort.class, ProductSortOptionDto.class);
    }
}
