package com.pe.advanced.bl.transformer.category;

import com.pe.advanced.domain.category.Category;
import com.pe.advanced.domain.category.NewCategory;
import com.pe.advanced.domain.transformers.AbstractTransformer;

public class NewCategoryTransformer extends AbstractTransformer<NewCategory, Category> {

    public static final NewCategoryTransformer instance = new NewCategoryTransformer();

    private NewCategoryTransformer() {
        // Singleton
    }

    @Override
    public void copyToOutput(NewCategory dto, Category product) {
        super.copyToOutput(dto, product);

        product.setName(dto.getName());
        product.setActive(dto.isActive());
    }

    @Override
    public Category createOutput(NewCategory product) {
        if (product == null) {
            return null;
        }

        final var dto = new Category();
        copyToOutput(product, dto);
        return dto;
    }
}
