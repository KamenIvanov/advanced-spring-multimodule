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
    public void copyToOutput(NewCategory dto, Category category) {
        super.copyToOutput(dto, category);

        category.setName(dto.getName());
    }

    @Override
    public Category createOutput(NewCategory newCategory) {
        if (newCategory == null) {
            return null;
        }

        final var dto = new Category();
        copyToOutput(newCategory, dto);
        return dto;
    }
}
