package com.pe.advanced.dao.api.search;

import com.pe.advanced.dao.api.CrudDao;

public interface SearchableDao<IdType, Type, Query extends AbstractParams<IdType, ? extends SortBy>> extends CrudDao<IdType, Type> {

    ResultPage<Type> search(Query criteria);
}
