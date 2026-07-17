package com.pe.advanced.dao.api.category;

import com.pe.advanced.dao.api.SearchableDao;
import com.pe.advanced.domain.category.Category;

import java.util.UUID;

public interface CategoryDao extends SearchableDao<UUID, Category, CategorySearchQuery> {

}
