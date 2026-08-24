package com.pe.advanced.dao.impl;

import com.pe.advanced.dao.api.search.*;
import com.pe.advanced.domain.AbstractCreatable;
import com.pe.advanced.domain.transformers.BiTransformer;
import jakarta.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import static com.pe.advanced.dao.api.search.Paging.DEFAULT_SIZE;
import static com.pe.advanced.dao.api.search.Paging.FIRST_PAGE;

public abstract class SearchableDaoImpl<
        IdType,
        Domain extends AbstractCreatable<IdType>,
        Entity extends AbstractCreatableEntity,
        Repo extends CrudRepository<Entity, IdType> & JpaSpecificationExecutor<Entity>,
        Params extends AbstractParams<? extends SortBy>
    > extends CrudDaoImpl<IdType, Domain, Entity, Repo> implements SearchableDao<Domain, Params> {

    protected static final Sort ID_DESC_SORT = Sort.by(Sort.Direction.DESC, "id");
    protected static final Sort CREATED_AT_DESC_SORT = Sort.by("createdAt").descending().and(ID_DESC_SORT);

    protected SearchableDaoImpl(Repo repository, BiTransformer<Entity, Domain> transformer, Validator validator) {
        super(repository, transformer, validator);
    }

    @Override
    public ResultPage<Domain> search(Params params) {
        final Pageable pageable = toPage(params);
        final Specification<Entity> spec = params == null
                ? Specification.unrestricted()
                : createSpecification(params);
        final Page<Entity> result = repository.findAll(spec, pageable);
        final List<Domain> content = result.getContent()
                .stream()
                .map(transformer::createOutput)
                .toList();

        return new ResultPage<>(result.getTotalPages(), result.getTotalElements(), content);
    }

    private Pageable toPage(Params criteria) {
        if (criteria == null) {
            return PageRequest.of(FIRST_PAGE, DEFAULT_SIZE, CREATED_AT_DESC_SORT);
        }

        return PageRequest.of(
                criteria.getPage(),
                criteria.getSize(),
                getSortOrDefault(criteria.getSortBy(), criteria.getSortDirection())
        );
    }

    private Sort getSortOrDefault(SortBy field, SortDirection direction) {
        if (field == null) {
            return CREATED_AT_DESC_SORT;
        }

        var orderDirection = switch (direction) {
            case ASC -> Sort.Direction.ASC;
            default -> Sort.Direction.DESC; // Handles both null and DESC
        };

        // ID as tiebreaker ensures stable pagination when sortBy field has equal values
        return Sort.by(orderDirection, field.getPropertyName()).and(ID_DESC_SORT);
    }

    protected abstract Specification<Entity> createSpecification(Params params);
}
