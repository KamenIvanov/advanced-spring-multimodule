package com.pe.advanced.bl.service.impl.category;

import com.pe.advanced.bl.service.CategoriesService;
import com.pe.advanced.bl.service.impl.AbstractCrudService;
import com.pe.advanced.bl.transformer.category.NewCategoryTransformer;
import com.pe.advanced.bl.transformer.category.UpdateCategoryTransformer;
import com.pe.advanced.dao.api.category.CategoryDao;
import com.pe.advanced.dao.api.category.CategorySearchParams;
import com.pe.advanced.dao.api.category.CategorySort;
import com.pe.advanced.dao.api.search.ResultPage;
import com.pe.advanced.dao.api.search.SortDirection;
import com.pe.advanced.domain.category.Category;
import com.pe.advanced.domain.category.NewCategory;
import com.pe.advanced.domain.category.UpdateCategory;
import com.pe.advanced.domain.exceptions.AuthorizationException;
import com.pe.advanced.domain.transformers.Transformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class CategoriesServiceImpl extends AbstractCrudService<NewCategory, UpdateCategory, Category, CategoryDao> implements CategoriesService {

    public CategoriesServiceImpl(CategoryDao dao) {
        super(dao);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultPage<Category> getCategories(int page, int size, CategorySort sort, SortDirection direction) {
        final var query = new CategorySearchParams();
        query.setPage(page);
        query.setSize(size);
        query.setSortBy(sort);
        query.setSortDirection(direction);

        return getDao().search(query);
    }

    @Override
    protected void preProcessNewEntity(Category product, UUID requesterId) {
        product.setCreatedById(requesterId);
        product.setUpdatedById(requesterId);
    }

    @Override
    protected Transformer<NewCategory, Category> getCreateTransformer() {
        return NewCategoryTransformer.instance;
    }

    @Override
    protected Transformer<UpdateCategory, Category> getUpdateTransformer() {
        return UpdateCategoryTransformer.instance;
    }

    @Override
    protected void authorize(Category domain, UUID requesterId) {
        if (!domain.getCreatedById().equals(requesterId)) {
            throw new AuthorizationException(UNAUTHORIZED);
        }
    }
}
