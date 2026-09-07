package com.pe.advanced.bl.transformer.product;

import com.pe.advanced.domain.product.NewProduct;
import com.pe.advanced.domain.product.Product;
import com.pe.advanced.domain.product.ProductSpecification;
import com.pe.advanced.domain.transformers.AbstractTransformer;

public class NewProductTransformer extends AbstractTransformer<NewProduct, Product> {

    public static final NewProductTransformer instance = new NewProductTransformer();

    private NewProductTransformer() {
        // Singleton
    }

    @Override
    public void copyToOutput(NewProduct dto, Product product) {
        super.copyToOutput(dto, product);

        product.setName(dto.getName());
        product.setSku(dto.getSku());
        product.setPrice(dto.getPrice());
        product.setSpecification(new ProductSpecification(dto.getDimensions(), dto.getWeight()));
    }

    @Override
    public Product createOutput(NewProduct product) {
        if (product == null) {
            return null;
        }

        final var dto = new Product();
        copyToOutput(product, dto);
        return dto;
    }
}
