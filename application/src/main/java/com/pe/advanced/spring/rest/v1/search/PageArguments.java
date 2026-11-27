package com.pe.advanced.spring.rest.v1.search;

import com.pe.advanced.domain.exceptions.ValidationException;

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
    private static final int MAX_SIZE = 100;

    private PageArguments() {
        // Static
    }

    /**
     * An absent page is the first one. Anything below the first page is refused rather than
     * clamped: a caller counting from zero has misread the contract, and silently answering with
     * the first page teaches it that it read correctly.
     */
    public static int index(Integer page) {
        if (page == null) {
            return 0;
        }

        if (page < FIRST_PAGE) {
            throw new ValidationException("The first page is " + FIRST_PAGE + ".");
        }

        return page - FIRST_PAGE;
    }

    /**
     * The maximum is the one the contract documents. Without it the promise is decoration, and a
     * caller asking for a hundred thousand rows gets a full table scan instead of an answer.
     */
    public static int size(Integer size) {
        if (size == null) {
            return DEFAULT_SIZE;
        }

        if (size < 1 || size > MAX_SIZE) {
            throw new ValidationException("The page's size must be between 1 and " + MAX_SIZE + ".");
        }

        return size;
    }
}
