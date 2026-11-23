package com.pe.advanced.spring.transformers.product;

import com.pe.advanced.api.dto.product.ProductsDescriptionsDto;
import com.pe.advanced.dao.api.search.ResultPage;
import com.pe.advanced.domain.product.Product;
import com.pe.advanced.domain.transformers.AbstractTransformer;

import java.util.List;

public class ProductsDescriptionsTransformer extends AbstractTransformer<ResultPage<Product>, ProductsDescriptionsDto> {

    public static final ProductsDescriptionsTransformer instance = new ProductsDescriptionsTransformer();

    private ProductsDescriptionsTransformer() {
        // Singleton
    }

    @Override
    public ProductsDescriptionsDto createOutput(ResultPage<Product> page) {
        if (page == null) {
            return new ProductsDescriptionsDto(0, 0L, List.of());
        }

        return new ProductsDescriptionsDto(
                page.totalPages(),
                page.totalHits(),
                ProductDescriptionTransformer.instance.createOutput(page.elements())
        );
    }
}
