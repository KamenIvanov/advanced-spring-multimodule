package com.pe.advanced.spring.transformers.product;

import com.pe.advanced.api.dto.product.ProductDto;
import com.pe.advanced.domain.product.Product;
import com.pe.advanced.spring.transformers.AbstractUpdatableTransformer;

public class ProductTransformer extends AbstractUpdatableTransformer<Product, ProductDto> {

    public static final ProductTransformer instance = new ProductTransformer();

    private ProductTransformer() {
        // Singleton
    }

    @Override
    public void copyToOutput(Product product, ProductDto productDto) {
        super.copyToOutput(product, productDto);

        productDto.setName(product.getName());
        productDto.setSku(product.getSku());
        productDto.setPrice(product.getPrice());
        productDto.setStatus(ProductStatusTransformer.instance.createOutput(product.getStatus()));
        productDto.setSpecification(ProductSpecificationTransformer.instance.createOutput(product.getSpecification()));
    }

    @Override
    public ProductDto createOutput(Product product) {
        if (product == null) {
            return null;
        }

        final var dto = new ProductDto();
        copyToOutput(product, dto);
        return dto;
    }

}
