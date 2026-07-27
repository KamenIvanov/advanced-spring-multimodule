package com.pe.advanced.dao.api.category;

import com.pe.advanced.dao.api.CrudDao;
import com.pe.advanced.dao.api.search.SearchableDao;
import com.pe.advanced.domain.category.Category;

import java.util.UUID;

public interface CategoryDao extends CrudDao<UUID, Category>, SearchableDao<Category, CategorySearchParams> {

}
