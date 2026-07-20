package com.pe.advanced.dao.impl.transformers.product;

import com.pe.advanced.dao.impl.product.ProductSpecificationEntity;
import com.pe.advanced.dao.impl.transformers.AbstractUpdatableEntityTransformer;
import com.pe.advanced.domain.product.ProductSpecification;

public class ProductSpecificationTransformer extends AbstractUpdatableEntityTransformer<ProductSpecificationEntity, ProductSpecification> {

    public static final ProductSpecificationTransformer instance = new ProductSpecificationTransformer();

    private ProductSpecificationTransformer() {
        // POJO
    }

    @Override
    public void copyToInput(ProductSpecification dest, ProductSpecificationEntity source) {
        super.copyToInput(dest, source);

        source.setDimension(dest.getDimensions());
        source.setWeight(dest.getWeight());
    }

    @Override
    public void copyToOutput(ProductSpecificationEntity source, ProductSpecification dest) {
        super.copyToOutput(source, dest);

        dest.setDimensions(source.getDimension());
        dest.setWeight(source.getWeight());
    }

    @Override
    public ProductSpecificationEntity createInput(ProductSpecification specification) {
        if (specification == null) {
            return null;
        }

        final var entity = new ProductSpecificationEntity();
        copyToInput(specification, entity);
        return entity;
    }

    @Override
    public ProductSpecification createOutput(ProductSpecificationEntity entity) {
        if (entity == null) {
            return null;
        }

        final var specification = new ProductSpecification();
        copyToOutput(entity, specification);
        return specification;
    }
}
