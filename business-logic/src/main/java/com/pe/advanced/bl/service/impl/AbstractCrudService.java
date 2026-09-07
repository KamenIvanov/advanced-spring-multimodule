package com.pe.advanced.bl.service.impl;

import com.pe.advanced.bl.service.CrudService;
import com.pe.advanced.dao.api.CrudDao;
import com.pe.advanced.domain.AbstractAuditable;
import com.pe.advanced.domain.exceptions.AuthorizationException;
import com.pe.advanced.domain.exceptions.NotFoundException;
import com.pe.advanced.domain.transformers.Transformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public abstract class AbstractCrudService<
        CreateDomain,
        UpdateDomain,
        Domain extends AbstractAuditable<UUID>,
        Dao extends CrudDao<UUID, Domain>
    > extends AbstractService implements CrudService<CreateDomain, UpdateDomain, Domain> {

    private final Dao dao;

    protected AbstractCrudService(Dao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional
    public Domain create(CreateDomain newDomain, UUID requesterId) {
        if (requesterId == null) {
            throw new AuthorizationException(UNAUTHORIZED);
        }

        final var domain = getCreateTransformer().createOutput(newDomain);
        domain.setCreatedById(requesterId);
        domain.setUpdatedById(requesterId);

        return dao.create(domain);
    }

    @Override
    @Transactional
    public Domain update(UUID id, UpdateDomain updateDomain, UUID requesterId) {
        if (requesterId == null) {
            throw new AuthorizationException(UNAUTHORIZED);
        }

        if (id == null) {
            throw new NotFoundException(MISSING_ENTITY);
        }

        final var domain = loadOrThrowNotFound(() -> dao.loadById(id));

        // Can the requester modify the entity?
        authorize(domain, requesterId);
        //
        getUpdateTransformer().copyToOutput(updateDomain, domain);
        domain.setUpdatedById(requesterId);
        return dao.update(domain);
    }

    @Override
    @Transactional(readOnly = true)
    public Domain loadById(UUID id, UUID requesterId) {
        if (requesterId == null) {
            throw new AuthorizationException(UNAUTHORIZED);
        }

        final var domain = loadOrThrowNotFound(() -> dao.loadById(id));
        authorize(domain, requesterId);
        return domain;
    }

    @Override
    @Transactional
    public void delete(UUID id, UUID requesterId) {
        if (requesterId == null) {
            throw new AuthorizationException(UNAUTHORIZED);
        }

        final var domain = dao.loadById(id);

        if (domain == null) {
            return;
        }

        authorizeDelete(domain, requesterId);
        dao.delete(domain);
    }

    protected Dao getDao() {
        return dao;
    }

    protected void authorizeDelete(Domain domain, UUID requesterId) {
        this.authorize(domain, requesterId);
    }

    protected abstract Transformer<CreateDomain, Domain> getCreateTransformer();

    protected abstract Transformer<UpdateDomain, Domain> getUpdateTransformer();

    protected abstract void authorize(Domain domain, UUID requesterId);
}
