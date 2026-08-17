package com.pe.advanced.dao.impl;

import com.pe.advanced.dao.api.CrudDao;
import com.pe.advanced.domain.AbstractCreatable;
import com.pe.advanced.domain.asserter.DeepEqualsAsserter;
import com.pe.advanced.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractCrudTestCase<
        ID,
        D extends AbstractCreatable<ID>,
        DAO extends CrudDao<ID, D>
    > extends AbstractDaoTest {

    @Test
    void testCreate() {
        final D expectedEntity = runSave();
        final D dbEntity = getDao().loadById(expectedEntity.getId());
        assertNotNull(dbEntity, "missing entity");
        assertEquals(expectedEntity.getId(), dbEntity.getId());
        final D reloaded = getDao().loadById(expectedEntity.getId());
        getAsserter().assertDeepEquals(reloaded, dbEntity);
    }

    @Test
    void testCreateNull() {
        final var ex = Assertions.assertThrows(IllegalArgumentException.class, () -> getDao().create(null));
        Assertions.assertEquals("Entity is mandatory", ex.getMessage());
    }

    @Test
    void testCreateExisting() {
        final D existing = runSave();

        final var ex = Assertions.assertThrows(IllegalArgumentException.class, () -> getDao().create(existing));
        Assertions.assertEquals("Entity existing!", ex.getMessage());
    }

    @Test
    void testUpdate() {
        final D saved = runSave();

        final D updated = getDao().update(updateDomain(saved));
        assertNotNull(updated, "missing entity");
        assertEquals(saved.getId(), updated.getId());

        final D reloaded = getDao().loadById(saved.getId());
        assertNotNull(reloaded, "entity is missing");
        getAsserter().assertDeepEquals(updated, reloaded);
    }

    @Test
    void testUpdateNull() {
        final var ex = Assertions.assertThrows(IllegalArgumentException.class, () -> getDao().update(null));
        Assertions.assertEquals("Entity is mandatory", ex.getMessage());
    }

    @Test
    void testUpdateNullId() {
        final var ex = Assertions.assertThrows(IllegalArgumentException.class, () -> getDao().update(createDomain()));
        Assertions.assertEquals("Entity id is mandatory", ex.getMessage());
    }

    @Test
    void testUpdateNotFound() {
        final D saved = runSave();
        getDao().delete(getDao().loadById(saved.getId()));
        assertNull(getDao().loadById(saved.getId()), "entity is available");

        Assertions.assertThrows(NotFoundException.class, () -> getDao().update(saved));
    }

    @Test
    protected void testDelete() {
        final D entity = runSave();

        final D savedEntity = getDao().loadById(entity.getId());
        assertNotNull(savedEntity, "entity is missing");
        getDao().delete(savedEntity);

        assertNull(getDao().loadById(entity.getId()), "entity is available");
    }

    @Test
    protected void testDeleteNull() {
        final var ex = Assertions.assertThrows(IllegalArgumentException.class, () -> getDao().delete(null));
        Assertions.assertEquals("Entity is mandatory", ex.getMessage());
    }

    @Test
    void testDeleteNullId() {
        final var ex = Assertions.assertThrows(NullPointerException.class, () -> getDao().delete(createDomain()));
        Assertions.assertEquals("ID must not be null", ex.getMessage());
    }

    @Test
    void testDeleteNonExistent() {
        final D saved = runSave();
        final D loaded = getDao().loadById(saved.getId());
        getDao().delete(loaded);
        assertNull(getDao().loadById(saved.getId()), "entity is available");

        // deleting again is a no-op: the id is set but the row no longer exists
        assertDoesNotThrow(() -> getDao().delete(saved));
    }

    @Test
    void testLoadById() {
        final D entity = runSave();
        final D dbEntity = getDao().loadById(entity.getId());
        assertNotNull(dbEntity, "entity is missing");
    }

    @Test
    void testLoadByIdNullArg() {
        final D dbEntity = getDao().loadById(null);
        assertNull(dbEntity);
    }

    protected D runSave() {
        final D expected = createDomain();
        return runSave(expected);
    }

    protected D runSave(D expected) {
        assertNull(getDao().loadById(expected.getId()), "entity is available");
        final var saved = getDao().create(expected);
        return getDao().loadById(saved.getId());
    }

    /**
     * Hook that lets concrete test cases mutate a persisted domain before it is passed to
     * {@link CrudDao#update(AbstractCreatable)}. The default implementation returns the domain unchanged,
     * which still exercises the update path end-to-end.
     *
     * @param domain the persisted domain to be updated
     * @return the domain to update with
     */
    protected D updateDomain(D domain) {
        return domain;
    }

    protected abstract DAO getDao();

    public abstract D createDomain();

    protected abstract DeepEqualsAsserter<D> getAsserter();
}
