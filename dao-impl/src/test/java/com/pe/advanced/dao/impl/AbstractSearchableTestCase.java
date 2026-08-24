package com.pe.advanced.dao.impl;

import com.pe.advanced.dao.api.CrudDao;
import com.pe.advanced.dao.api.search.AbstractParams;
import com.pe.advanced.dao.api.search.ResultPage;
import com.pe.advanced.dao.api.search.SearchableDao;
import com.pe.advanced.dao.api.search.SortBy;
import com.pe.advanced.domain.AbstractCreatable;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractSearchableTestCase<
        ID,
        Domain extends AbstractCreatable<ID>,
        Dao extends CrudDao<ID, Domain> & SearchableDao<Domain, Params>,
        Params extends AbstractParams<? extends SortBy>
    > extends AbstractCrudTestCase<ID, Domain, Dao> {

    @Test
    void testSearchAll() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            runSave(createDomainWithUniqueData());
            TimeUnit.MILLISECONDS.sleep(20);
        }

        final Params params = createSearchParams();
        params.setPage(0);
        params.setSize(10);

        final ResultPage<Domain> result = getDao().search(params);

        assertEquals(5, result.elements().size());
        assertTrue(result.totalHits() >= 5);
        assertEquals(1, result.totalPages());

        // Assert default order by createdAt descending
        final List<Domain> allElements = result.elements();
        for (int i = 0; i < allElements.size() - 1; i++) {
            assertTrue(
                    allElements.get(i).getCreatedAt().compareTo(allElements.get(i + 1).getCreatedAt()) >= 0,
                    "Elements should be ordered by createdAt descending"
            );
        }
    }

    @Test
    void testSearchPageAndSize() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            runSave(createDomainWithUniqueData());
            TimeUnit.MILLISECONDS.sleep(20);
        }

        final Params params = createSearchParams();
        params.setPage(0);
        params.setSize(2);

        final ResultPage<Domain> result = getDao().search(params);

        assertEquals(2, result.elements().size());
        assertTrue(result.totalHits() >= 5);

        params.setPage(1);
        final ResultPage<Domain> resultPage2 = getDao().search(params);
        assertEquals(2, resultPage2.elements().size());
        assertNotEquals(result.elements().getFirst().getId(), resultPage2.elements().getFirst().getId());
    }

    @Test
    void testSearchOutOfRange() {
        for (int i = 0; i < 5; i++) {
            runSave(createDomainWithUniqueData());
        }

        final Params params = createSearchParams();
        params.setPage(999);
        params.setSize(10);

        final ResultPage<Domain> result = getDao().search(params);

        assertEquals(1, result.totalPages());
        assertEquals(5, result.totalHits());
        assertTrue(result.elements().isEmpty());
    }

    @Test
    void testSearchZeroSize() {
        for (int i = 0; i < 5; i++) {
            runSave(createDomainWithUniqueData());
        }

        final Params params = createSearchParams();
        params.setPage(0);
        params.setSize(0);
        assertThrows(IllegalArgumentException.class, () -> getDao().search(params));
    }

    @Test
    public void testSearchNullQuery() {
        for (int i = 0; i < 5; i++) {
            runSave(createDomainWithUniqueData());
        }

        final var results = getDao().search(null);
        assertEquals(5, results.elements().size());
    }

    protected Domain createDomainWithUniqueData() {
        return createDomain();
    }

    protected abstract Params createSearchParams();
}
