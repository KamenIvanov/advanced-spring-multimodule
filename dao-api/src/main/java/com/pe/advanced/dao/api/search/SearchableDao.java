package com.pe.advanced.dao.api.search;

import com.pe.advanced.dao.api.CrudDao;

public interface SearchableDao<IdType, Type, Params extends AbstractParams<? extends SortBy>> extends CrudDao<IdType, Type> {

    ResultPage<Type> search(Params criteria);
}
