package com.pe.advanced.dao.api.search;

public class Paging {

    private static final int FIRST_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;

    private int page = FIRST_PAGE;
    private int size = DEFAULT_SIZE;

    public Paging() {
        // POJO
    }

    public Paging(int page, int size) {
        setPage(page);
        setSize(size);
    }

    public int getPage() {
        return page;
    }

    public void setPage(Integer page) {
        if (page == null || page < FIRST_PAGE) {
            this.page = FIRST_PAGE;
        } else {
            this.page = page;
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(Integer size) {
        if (size == null || size <= 0) {
            this.size = DEFAULT_SIZE;
        } else {
            this.size = size;
        }
    }
}
