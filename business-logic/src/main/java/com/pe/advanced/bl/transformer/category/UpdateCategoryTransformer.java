package com.pe.advanced.bl.transformer.category;

import com.pe.advanced.domain.category.Category;
import com.pe.advanced.domain.category.UpdateCategory;
import com.pe.advanced.domain.transformers.AbstractTransformer;

public class UpdateCategoryTransformer extends AbstractTransformer<UpdateCategory, Category> {

    public static final UpdateCategoryTransformer instance = new UpdateCategoryTransformer();

    private UpdateCategoryTransformer() {
        // Singleton
    }

    @Override
    public void copyToOutput(UpdateCategory dto, Category category) {
        super.copyToOutput(dto, category);

        category.setName(dto.getName());
    }

    @Override
    public Category createOutput(UpdateCategory category) {
        if (category == null) {
            return null;
        }

        final var dto = new Category();
        copyToOutput(category, dto);
        return dto;
    }
}
