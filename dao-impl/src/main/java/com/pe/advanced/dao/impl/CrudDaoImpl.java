package com.pe.advanced.dao.impl;

import com.pe.advanced.dao.api.CrudDao;
import com.pe.advanced.domain.AbstractCreatable;
import com.pe.advanced.domain.exceptions.NotFoundException;
import com.pe.advanced.domain.transformers.BiTransformer;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.util.Objects;

public abstract class CrudDaoImpl<
        IdType,
        Domain extends AbstractCreatable<IdType>,
        Entity extends AbstractCreatableEntity,
        Repo extends CrudRepository<Entity, IdType>
    > implements CrudDao<IdType, Domain> {

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
        preUpdate(entity);
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
        if (entity instanceof AbstractUpdatableEntity updatableEntity) {
            updatableEntity.setUpdatedAt(entity.getCreatedAt());
        }
    }

    protected void preUpdate(AbstractCreatableEntity entity) {
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
}
