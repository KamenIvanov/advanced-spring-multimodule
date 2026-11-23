package com.pe.advanced.spring.transformers.product;

import com.pe.advanced.api.dto.product.ProductDescriptionDto;
import com.pe.advanced.domain.product.Product;
import com.pe.advanced.spring.transformers.AbstractCreatableTransformer;

/**
 * The public projection. It copies the creation audit field the anonymous reader is allowed to
 * see and deliberately not the update one or the version, which is why it extends the creatable
 * transformer rather than the updatable one.
 */
public class ProductDescriptionTransformer extends AbstractCreatableTransformer<Product, ProductDescriptionDto> {

    public static final ProductDescriptionTransformer instance = new ProductDescriptionTransformer();

    private ProductDescriptionTransformer() {
        // Singleton
    }

    @Override
    public void copyToOutput(Product product, ProductDescriptionDto productDto) {
        super.copyToOutput(product, productDto);

        productDto.setName(product.getName());
        productDto.setSku(product.getSku());
        productDto.setPrice(product.getPrice());
        productDto.setStatus(ProductStatusTransformer.instance.createOutput(product.getStatus()));
    }

    @Override
    public ProductDescriptionDto createOutput(Product product) {
        if (product == null) {
            return null;
        }

        final var dto = new ProductDescriptionDto();
        copyToOutput(product, dto);
        return dto;
    }
}
