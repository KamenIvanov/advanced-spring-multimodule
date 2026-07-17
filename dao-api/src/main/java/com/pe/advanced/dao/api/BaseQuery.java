package com.pe.advanced.dao.api;

import java.util.List;

public abstract class BaseQuery<ID, Sortable extends SortableEnum> {

    protected List<ID> ids;
    protected int page;
    protected int size;
    private Sortable sortBy;
    private SortDirection sortDirection;

    protected BaseQuery() {
        this(0, 20);
    }

    protected BaseQuery(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public List<ID> getIds() {
        return ids;
    }

    public void setIds(List<ID> ids) {
        this.ids = ids;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Sortable getSortBy() {
        return sortBy;
    }

    public void setSortBy(Sortable sortBy) {
        this.sortBy = sortBy;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }
}
