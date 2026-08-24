package com.pe.advanced.dao.impl.proxy;

import com.pe.advanced.dao.api.CrudDao;
import com.pe.advanced.dao.api.search.AbstractParams;
import com.pe.advanced.dao.api.search.ResultPage;
import com.pe.advanced.dao.api.search.SearchableDao;
import com.pe.advanced.dao.api.search.SortBy;
import com.pe.advanced.domain.AbstractCreatable;
import jakarta.persistence.EntityManager;

public abstract class SearchableCrudDaoProxy<
        IdType,
        Domain extends AbstractCreatable<IdType>,
        Params extends AbstractParams<? extends SortBy>,
        Dao extends CrudDao<IdType, Domain> & SearchableDao<Domain, Params>
    > extends CrudDaoProxy<IdType, Domain, Dao> implements SearchableDao<Domain, Params> {

    protected SearchableCrudDaoProxy(Dao proxied, EntityManager entityManager) {
        super(proxied, entityManager);
    }

    @Override
    public ResultPage<Domain> search(Params params) {
        return proxied.search(params);
    }
}
