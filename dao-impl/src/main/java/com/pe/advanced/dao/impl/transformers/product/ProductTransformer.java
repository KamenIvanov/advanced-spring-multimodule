package com.pe.advanced.dao.impl.transformers.product;

import com.pe.advanced.dao.impl.product.ProductEntity;
import com.pe.advanced.dao.impl.transformers.AbstractAuditableEntityTransformer;
import com.pe.advanced.domain.product.Product;

public class ProductTransformer extends AbstractAuditableEntityTransformer<ProductEntity, Product> {

    public static final ProductTransformer instance = new ProductTransformer();

    private ProductTransformer() {
        // POJO
    }

    @Override
    public void copyToInput(Product dest, ProductEntity source) {
        super.copyToInput(dest, source);

        source.setName(dest.getName());
        source.setSku(dest.getSku());
        source.setStatus(ProductTypeTransformer.instance.createInput(dest.getStatus()));
        source.setPrice(dest.getPrice());
        source.setSpecification(ProductSpecificationTransformer.instance.createInput(dest.getSpecification()));
    }

    @Override
    public void copyToOutput(ProductEntity source, Product dest) {
        super.copyToOutput(source, dest);

        dest.setName(source.getName());
        dest.setSku(source.getSku());
        dest.transitionTo(ProductTypeTransformer.instance.createOutput(source.getStatus()));
        dest.setPrice(source.getPrice());
        dest.setSpecification(ProductSpecificationTransformer.instance.createOutput(source.getSpecification()));
    }

    @Override
    public ProductEntity createInput(Product product) {
        if (product == null) {
            return null;
        }

        final var entity = new ProductEntity();
        copyToInput(product, entity);
        return entity;
    }

    @Override
    public Product createOutput(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        final var product = new Product();
        copyToOutput(entity, product);
        return product;
    }
}
