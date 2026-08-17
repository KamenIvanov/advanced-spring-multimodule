package com.pe.advanced.dao.impl.category;

import com.pe.advanced.dao.api.category.CategoryDao;
import com.pe.advanced.dao.api.category.CategorySearchParams;
import com.pe.advanced.dao.impl.SearchableDaoImpl;
import com.pe.advanced.dao.impl.transformers.category.CategoryTransformer;
import com.pe.advanced.domain.category.Category;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Validator;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryDaoImpl extends SearchableDaoImpl<
        UUID,
        Category,
        CategoryEntity,
        CategoryRepository,
        CategorySearchParams
        > implements CategoryDao {

    public CategoryDaoImpl(CategoryRepository repository, Validator validator) {
        super(repository, CategoryTransformer.instance, validator);
    }

    @Override
    protected Specification<CategoryEntity> createSpecification(CategorySearchParams params) {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (params.getName() != null && !params.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + params.getName().toLowerCase() + "%"));
            }

            if (params.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), params.getActive()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
