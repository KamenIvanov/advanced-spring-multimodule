package com.pe.advanced.dao.impl;

import com.pe.advanced.dao.api.CrudDao;
import com.pe.advanced.dao.api.search.Paging;
import com.pe.advanced.dao.api.search.SortBy;
import com.pe.advanced.dao.api.search.SortDirection;
import com.pe.advanced.domain.AbstractCreatable;
import com.pe.advanced.domain.exceptions.NotFoundException;
import com.pe.advanced.domain.transformers.BiTransformer;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.util.Objects;

public abstract class CrudDaoImpl<
        IdType,
        Domain extends AbstractCreatable<IdType>,
        Entity extends AbstractCreatableEntity,
        Repo extends CrudRepository<Entity, IdType>
    > implements CrudDao<IdType, Domain> {

    protected static final PageRequest DEFAULT_PAGE = PageRequest.of(0, 10);
    protected static final Sort ID_DESC_SORT = Sort.by(Sort.Direction.DESC, "id");
    protected static final Sort CREATED_AT_DESC_SORT = Sort.by("createdAt").descending().and(ID_DESC_SORT);

    protected final Validator validator;

    protected final Repo repository;
    protected final BiTransformer<Entity, Domain> transformer;

    protected CrudDaoImpl(Repo repository, BiTransformer<Entity, Domain> transformer, Validator validator) {
        this.repository = repository;
        this.transformer = transformer;
        this.validator = validator;
    }

    @Override
    public Domain create(Domain domain) {
        if (domain == null) {
            throw new IllegalArgumentException("Entity is mandatory");
        }

        if (domain.getId() != null) {
            throw new IllegalArgumentException("Entity existing!");
        }

        var entity = transformer.createInput(domain);
        preCreate(entity);
        validateEntity(entity);
        entity = repository.save(entity);
        return transformer.createOutput(entity);
    }

    @Override
    public Domain update(Domain domain) {
        if (domain == null) {
            throw new IllegalArgumentException("Entity is mandatory");
        }

        if (domain.getId() == null) {
            throw new IllegalArgumentException("Entity id is mandatory");
        }

        var entity = repository.findById(domain.getId())
                .orElseThrow(() -> new NotFoundException("Entity with id '" + domain.getId() + "' not found."));

        transformer.copyToInput(domain, entity);
        updatableEntity(entity);
        entity = repository.save(entity);

        return transformer.createOutput(entity);
    }

    @Override
    public Domain loadById(IdType id) {
        if (id == null) {
            return null;
        }

        final var entity = repository.findById(id).orElse(null);
        if (entity == null) {
            return null;
        }
        return transformer.createOutput(entity);
    }

    @Override
    public void delete(Domain domain) {
        if (domain == null) {
            throw new IllegalArgumentException("Entity is mandatory");
        }
        Objects.requireNonNull(domain.getId(), "ID must not be null");
        repository.findById(domain.getId()).ifPresent(repository::delete);
    }

    protected void preCreate(AbstractCreatableEntity entity) {
        entity.setCreatedAt(Instant.now());
    }

    protected void updatableEntity(AbstractCreatableEntity entity) {
        if (entity instanceof AbstractUpdatableEntity updatableEntity) {
            updatableEntity.setUpdatedAt(Instant.now());
        }
    }

    protected void validateEntity(Entity entity) {
        final var violations = validator.validate(entity);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    protected Sort getSortOrDefault(SortBy field, SortDirection direction) {
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

    protected PageRequest toSpringPage(Paging paging) {
        if (paging == null) {
            return DEFAULT_PAGE;
        }

        // page is minus one, because spring's first page is 0, and we have 1
        return PageRequest.of(paging.getPage(), paging.getSize());
    }
}
