package com.pe.advanced.dao.api;

import java.util.List;

/**
 * Holds the requested page results.
 *
 * {@link ResultPage#totalPages} shows the total pages of results with the requested page size.
 * {@link ResultPage#totalHits} shows how many results are a total hit of the requested query.
 * {@link ResultPage#elements} are the current page's results.
 */
public record ResultPage<T>(int totalPages, long totalHits, List<T> elements) {
}