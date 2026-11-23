package com.pe.advanced.spring.transformers.product;

import com.pe.advanced.api.dto.product.ProductSpecificationDto;
import com.pe.advanced.domain.product.ProductSpecification;
import com.pe.advanced.spring.transformers.AbstractUpdatableTransformer;

public class ProductSpecificationTransformer extends AbstractUpdatableTransformer<ProductSpecification, ProductSpecificationDto> {

    public static final ProductSpecificationTransformer instance = new ProductSpecificationTransformer();

    private ProductSpecificationTransformer() {
        // Singleton
    }

    @Override
    public void copyToOutput(ProductSpecification spec, ProductSpecificationDto specDto) {
        super.copyToOutput(spec, specDto);

        specDto.setDimensions(spec.getDimensions());
        specDto.setWeight(spec.getWeight());
    }

    @Override
    public ProductSpecificationDto createOutput(ProductSpecification spec) {
        if (spec == null) {
            return null;
        }

        final var dto = new ProductSpecificationDto();
        copyToOutput(spec, dto);
        return dto;
    }

}
