package com.pe.advanced.dao.api.search;

public abstract class AbstractParams<Sortable extends SortBy> {

    protected int page;
    protected int size;
    private Sortable sortBy;
    private SortDirection sortDirection = SortDirection.ASC;

    protected AbstractParams() {
        this(0, 20);
    }

    protected AbstractParams(int page, int size) {
        this.page = Math.max(page, 0);
        this.size = size < 1 ? 10 : size;
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
