package com.pe.advanced.dao.api;

public interface SearchableDao<IdType, Type, Query extends BaseQuery<IdType, ? extends SortableEnum>> extends CrudDao<IdType, Type> {

    ResultPage<Type> search(Query criteria);
}
