package com.pe.advanced.bl.service;

import com.pe.advanced.dao.api.category.CategorySort;
import com.pe.advanced.dao.api.search.ResultPage;
import com.pe.advanced.dao.api.search.SortDirection;
import com.pe.advanced.domain.category.Category;
import com.pe.advanced.domain.category.NewCategory;
import com.pe.advanced.domain.category.UpdateCategory;

public interface CategoriesService extends CrudService<NewCategory, UpdateCategory, Category> {

    ResultPage<Category> getCategories(int page, int size, CategorySort sort, SortDirection direction);
}
