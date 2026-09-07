package com.pe.advanced.bl.transformer.product;

import com.pe.advanced.domain.product.Product;
import com.pe.advanced.domain.product.ProductSpecification;
import com.pe.advanced.domain.product.UpdateProduct;
import com.pe.advanced.domain.transformers.AbstractTransformer;

public class UpdateProductTransformer extends AbstractTransformer<UpdateProduct, Product> {

    public static final UpdateProductTransformer instance = new UpdateProductTransformer();

    private UpdateProductTransformer() {
        // Singleton
    }

    @Override
    public void copyToOutput(UpdateProduct dto, Product product) {
        super.copyToOutput(dto, product);

        product.setName(dto.getName());
        product.setSku(dto.getSku());
        product.setPrice(dto.getPrice());

        if (product.getSpecification() == null) {
            product.setSpecification(new ProductSpecification(dto.getDimensions(), dto.getWeight()));
        } else {
            product.getSpecification().setDimensions(dto.getDimensions());
            product.getSpecification().setWeight(dto.getWeight());
        }
    }

    @Override
    public Product createOutput(UpdateProduct product) {
        if (product == null) {
            return null;
        }

        final var dto = new Product();
        copyToOutput(product, dto);
        return dto;
    }
}
