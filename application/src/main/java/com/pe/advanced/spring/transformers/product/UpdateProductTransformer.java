package com.pe.advanced.spring.transformers.product;

import com.pe.advanced.api.dto.product.UpdateProductDto;
import com.pe.advanced.domain.product.UpdateProduct;
import com.pe.advanced.domain.transformers.AbstractTransformer;

public class UpdateProductTransformer extends AbstractTransformer<UpdateProductDto, UpdateProduct> {

    public static final UpdateProductTransformer instance = new UpdateProductTransformer();

    private UpdateProductTransformer() {
        // Singleton
    }

    @Override
    public void copyToOutput(UpdateProductDto updateProductDto, UpdateProduct updateProduct) {
        updateProduct.setName(updateProductDto.getName());
        updateProduct.setSku(updateProductDto.getSku());
        updateProduct.setPrice(updateProductDto.getPrice());
        updateProduct.setDimensions(updateProductDto.getDimensions());
        updateProduct.setWeight(updateProductDto.getWeight());
    }

    @Override
    public UpdateProduct createOutput(UpdateProductDto updateProductDto) {
        if (updateProductDto == null) {
            return null;
        }

        final var updateProduct = new UpdateProduct();
        copyToOutput(updateProductDto, updateProduct);
        return updateProduct;
    }
}
