package com.pe.advanced.dao.api.search;

public interface SearchableDao<Type, Params extends AbstractParams<? extends SortBy>> {

    ResultPage<Type> search(Params criteria);
}
