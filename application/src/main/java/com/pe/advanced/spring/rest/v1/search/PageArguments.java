package com.pe.advanced.spring.rest.v1.search;

/**
 * The two paging values as the layer behind the web wants them.
 * <p>
 * The contract numbers pages from one, because that is what a reader of a catalogue counts in,
 * and the search layer numbers them from zero. The subtraction lives here and nowhere else.
 * <p>
 * Both arrive as objects rather than primitives, because a query string can leave a parameter
 * out and {@code page=} binds to null rather than to the field's default.
 */
public final class PageArguments {

    private static final int FIRST_PAGE = 1;
    private static final int DEFAULT_SIZE = 20;

    private PageArguments() {
        // Static
    }

    public static int index(Integer page) {
        return page == null ? 0 : page - FIRST_PAGE;
    }

    public static int size(Integer size) {
        return size == null ? DEFAULT_SIZE : size;
    }
}
