package com.pe.advanced.spring.transformers.product;

import com.pe.advanced.api.dto.product.ProductsDto;
import com.pe.advanced.dao.api.search.ResultPage;
import com.pe.advanced.domain.product.Product;
import com.pe.advanced.domain.transformers.AbstractTransformer;

import java.util.List;

public class ProductsTransformer extends AbstractTransformer<ResultPage<Product>, ProductsDto> {

    public static final ProductsTransformer instance = new ProductsTransformer();

    private ProductsTransformer() {
        // Singleton
    }

    @Override
    public ProductsDto createOutput(ResultPage<Product> page) {
        if (page == null) {
            return new ProductsDto(0, 0L, List.of());
        }

        return new ProductsDto(
                page.totalPages(),
                page.totalHits(),
                ProductTransformer.instance.createOutput(page.elements())
        );
    }
}
