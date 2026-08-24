package com.pe.advanced.dao.impl.proxy;

import com.pe.advanced.dao.api.CrudDao;
import com.pe.advanced.domain.AbstractCreatable;
import jakarta.persistence.EntityManager;

public abstract class CrudDaoProxy<IdType, Domain extends AbstractCreatable<IdType>, Dao extends CrudDao<IdType, Domain>> implements CrudDao<IdType, Domain> {

    protected final Dao proxied;
    private final EntityManager entityManager;

    protected CrudDaoProxy(Dao proxied, EntityManager entityManager) {
        this.proxied = proxied;
        this.entityManager = entityManager;
    }

    @Override
    public Domain create(Domain domain) {
        final var saved = proxied.create(domain);
        flushAndClear();
        return saved;
    }

    @Override
    public Domain update(Domain domain) {
        final var updated = proxied.update(domain);
        flushAndClear();
        return updated;
    }

    @Override
    public Domain loadById(IdType id) {
        return proxied.loadById(id);
    }

    @Override
    public void delete(Domain domain) {
        proxied.delete(domain);
        flushAndClear();
    }

    protected void flushAndClear() {
        entityManager.flush(); // Forces SQL out of Hibernate write-behind cache
        entityManager.clear(); // Wipes L1 cache, forcing real DB hydration on next load
    }
}