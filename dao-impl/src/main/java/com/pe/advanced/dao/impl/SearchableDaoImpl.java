package com.pe.advanced.dao.impl;

import com.pe.advanced.dao.api.search.AbstractParams;
import com.pe.advanced.dao.api.search.ResultPage;
import com.pe.advanced.dao.api.search.SearchableDao;
import com.pe.advanced.dao.api.search.SortBy;
import com.pe.advanced.domain.AbstractCreatable;
import com.pe.advanced.domain.transformers.BiTransformer;
import jakarta.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public abstract class SearchableDaoImpl<
        IdType,
        Domain extends AbstractCreatable<IdType>,
        Entity extends AbstractCreatableEntity,
        Repo extends CrudRepository<Entity, IdType> & JpaSpecificationExecutor<Entity>,
        Params extends AbstractParams<? extends SortBy>
    > extends CrudDaoImpl<IdType, Domain, Entity, Repo> implements SearchableDao<Domain, Params> {

    protected SearchableDaoImpl(Repo repository, BiTransformer<Entity, Domain> transformer, Validator validator) {
        super(repository, transformer, validator);
    }

    @Override
    public ResultPage<Domain> search(Params params) {
        final Pageable pageable = toPage(params);
        final Specification<Entity> spec = createSpecification(params);
        final Page<Entity> result = repository.findAll(spec, pageable);
        final List<Domain> content = result.getContent()
                .stream()
                .map(transformer::createOutput)
                .toList();

        return new ResultPage<>(result.getTotalPages(), result.getTotalElements(), content);
    }

    private Pageable toPage(Params criteria) {
        return PageRequest.of(
                criteria.getPage(),
                criteria.getSize(),
                getSortOrDefault(criteria.getSortBy(), criteria.getSortDirection())
        );
    }

    protected abstract Specification<Entity> createSpecification(Params params);
}
