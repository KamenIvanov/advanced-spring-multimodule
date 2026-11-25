package com.pe.advanced.spring.transformers.product;

import com.pe.advanced.api.dto.product.NewProductDto;
import com.pe.advanced.domain.product.NewProduct;
import com.pe.advanced.domain.transformers.AbstractTransformer;

public class NewProductTransformer extends AbstractTransformer<NewProductDto, NewProduct> {

    public static final NewProductTransformer instance = new NewProductTransformer();

    private NewProductTransformer() {
        // Singleton
    }

    @Override
    public void copyToOutput(NewProductDto newProductDto, NewProduct newProduct) {
        newProduct.setName(newProductDto.getName());
        newProduct.setSku(newProductDto.getSku());
        newProduct.setPrice(newProductDto.getPrice());
        newProduct.setDimensions(newProductDto.getDimensions());
        newProduct.setWeight(newProductDto.getWeight());
    }

    @Override
    public NewProduct createOutput(NewProductDto newProductDto) {
        if (newProductDto == null) {
            return null;
        }

        final var newProduct = new NewProduct();
        copyToOutput(newProductDto, newProduct);
        return newProduct;
    }
}
