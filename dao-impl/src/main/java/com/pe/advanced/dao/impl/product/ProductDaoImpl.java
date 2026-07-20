package com.pe.advanced.dao.impl.product;

import com.pe.advanced.dao.api.product.ProductDao;
import com.pe.advanced.dao.api.product.ProductSearchParams;
import com.pe.advanced.dao.impl.SearchableDaoImpl;
import com.pe.advanced.dao.impl.transformers.product.ProductTransformer;
import com.pe.advanced.domain.product.Product;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Validator;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductDaoImpl extends SearchableDaoImpl<
        UUID,
        Product,
        ProductEntity,
        ProductRepository,
        ProductSearchParams
    > implements ProductDao {

    public ProductDaoImpl(ProductRepository repository, Validator validator) {
        super(repository, ProductTransformer.instance, validator);
    }

    @Override
    public Product loadBySku(String sku) {
        final var product = repository.loadBySku(sku);
        return transformer.createOutput(product);
    }

    @Override
    protected Specification<ProductEntity> createSpecification(ProductSearchParams params) {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if(params.getName() != null && !params.getName().isBlank()){
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + params.getName().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
