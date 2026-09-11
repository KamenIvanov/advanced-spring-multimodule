package com.pe.advanced.bl.service;

import com.pe.advanced.dao.api.CrudDao;
import com.pe.advanced.domain.AbstractAuditable;
import com.pe.advanced.domain.exceptions.AuthorizationException;
import com.pe.advanced.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public abstract class AbstractCrudServiceTestCase<
        Domain extends AbstractAuditable<UUID>,
        NewDomain,
        UpdateDomain,
        Dao extends CrudDao<UUID, Domain>,
        Service extends CrudService<NewDomain, UpdateDomain, Domain>
        > extends AbstractServiceTestCase {

    @Test
    void create_WhenRequesterIdIsNull_ShouldThrowAuthorizationException() {
        final NewDomain newDomain = createValidNewDomain();
        assertThrows(AuthorizationException.class, () -> getService().create(newDomain, null));
        verifyNoInteractions(getMockDao());
    }

    @Test
    void create_WhenRequesterIdIsPresent_ShouldStampOwnershipBeforePersisting() {
        final NewDomain newDomain = createValidNewDomain();
        final UUID assignedId = UUID.randomUUID();

        when(getMockDao().create(any())).thenAnswer(invocation -> withId(invocation.getArgument(0), assignedId));

        final Domain result = getService().create(newDomain, requesterId);

        final ArgumentCaptor<Domain> captor = ArgumentCaptor.captor();
        verify(getMockDao(), times(1)).create(captor.capture());
        final Domain domainPassedToDao = captor.getValue();

        // A transient domain has no identity until the DAO assigns one - the service must never invent an id itself.
        assertNull(domainPassedToDao.getId());
        assertEquals(requesterId, domainPassedToDao.getCreatedById());
        assertEquals(requesterId, domainPassedToDao.getUpdatedById());
        assertEquals(assignedId, result.getId());
        assertEquals(requesterId, result.getCreatedById());
    }

    @Test
    void update_WhenRequesterIdIsNull_ShouldThrowAuthorizationException() {
        final UpdateDomain updateDomain = createUpdateDomain();
        assertThrows(AuthorizationException.class, () -> getService().update(UUID.randomUUID(), updateDomain, null));
        verifyNoInteractions(getMockDao());
    }

    @Test
    void update_WhenEntityIdIsNull_ShouldThrowNotFoundException() {
        final UpdateDomain updateDomain = createUpdateDomain();
        assertThrows(NotFoundException.class, () -> getService().update(null, updateDomain, requesterId));
        verify(getMockDao(), never()).update(any());
    }

    @Test
    void update_WhenEntityDoesNotExist_ShouldThrowNotFoundException() {
        final UUID missingId = UUID.randomUUID();
        final UpdateDomain updateDomain = createUpdateDomain();

        when(getMockDao().loadById(missingId)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> getService().update(missingId, updateDomain, requesterId));
        verify(getMockDao(), never()).update(any());
    }

    @Test
    void update_WhenRequesterIsNotOwner_ShouldThrowAuthorizationExceptionAndNeverPersist() {
        final Domain existingDomain = persistedEntity(alternativeRequesterId);
        final UpdateDomain updateDomain = createUpdateDomain();

        when(getMockDao().loadById(existingDomain.getId())).thenReturn(existingDomain);
        assertThrows(AuthorizationException.class, () -> getService().update(existingDomain.getId(), updateDomain, requesterId));
        verify(getMockDao(), never()).update(any());
    }

    @Test
    void update_WhenRequesterIsOwner_ShouldPersistWithUpdatedByChangedAndCreatedByPreserved() {
        final Domain existingDomain = persistedEntity(requesterId);
        final UpdateDomain updateDomain = createUpdateDomain();

        when(getMockDao().loadById(existingDomain.getId())).thenReturn(existingDomain);
        when(getMockDao().update(any())).thenAnswer(invocation -> invocation.getArgument(0));

        final Domain result = getService().update(existingDomain.getId(), updateDomain, requesterId);

        final ArgumentCaptor<Domain> captor = ArgumentCaptor.captor();
        verify(getMockDao(), times(1)).update(captor.capture());
        final Domain persisted = captor.getValue();

        assertEquals(existingDomain.getId(), persisted.getId());
        // Same actor happens to be both creator and updater here (authorize() enforces that),
        // but this still guards against the update transformer accidentally
        // clobbering createdById while copying fields.
        assertEquals(requesterId, persisted.getCreatedById());
        assertEquals(requesterId, persisted.getUpdatedById());
        assertEquals(existingDomain.getId(), result.getId());
    }

    @Test
    void loadById_WhenRequesterIdIsNull_ShouldThrowAuthorizationException() {
        assertThrows(AuthorizationException.class, () -> getService().loadById(UUID.randomUUID(), null));
        verifyNoInteractions(getMockDao());
    }

    @Test
    void loadById_WhenEntityDoesNotExist_ShouldThrowNotFoundException() {
        final UUID randomId = UUID.randomUUID();
        when(getMockDao().loadById(randomId)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> getService().loadById(randomId, requesterId));
    }

    @Test
    void loadById_WhenRequesterIsOwner_ShouldReturnDomain() {
        final Domain existingDomain = persistedEntity(requesterId);
        when(getMockDao().loadById(existingDomain.getId())).thenReturn(existingDomain);

        final Domain result = getService().loadById(existingDomain.getId(), requesterId);
        assertEquals(existingDomain.getId(), result.getId());
        verify(getMockDao(), times(1)).loadById(existingDomain.getId());
    }

    @Test
    void loadById_WhenRequesterIsNotOwner_ShouldThrowAuthorizationException() {
        final Domain existingEntity = persistedEntity(alternativeRequesterId);
        when(getMockDao().loadById(existingEntity.getId())).thenReturn(existingEntity);
        assertThrows(AuthorizationException.class, () -> getService().loadById(existingEntity.getId(), requesterId));
    }

    @Test
    void delete_WhenRequesterIdIsNull_ShouldThrowAuthorizationException() {
        assertThrows(AuthorizationException.class, () -> getService().delete(UUID.randomUUID(), null));
        verifyNoInteractions(getMockDao());
    }

    @Test
    void delete_WhenEntityDoesNotExist_ShouldBeANoOp() {
        final UUID randomId = UUID.randomUUID();
        when(getMockDao().loadById(randomId)).thenReturn(null);

        assertDoesNotThrow(() -> getService().delete(randomId, requesterId));

        verify(getMockDao(), times(1)).loadById(randomId);
        verify(getMockDao(), never()).delete(any());
    }

    @Test
    void delete_WhenRequesterIsOwner_ShouldDeleteEntity() {
        final Domain existingEntity = persistedEntity(requesterId);
        when(getMockDao().loadById(existingEntity.getId())).thenReturn(existingEntity);

        getService().delete(existingEntity.getId(), requesterId);
        verify(getMockDao(), times(1)).delete(existingEntity);
    }

    @Test
    void delete_WhenRequesterIsNotOwner_ShouldThrowAuthorizationExceptionAndNeverDelete() {
        final Domain existingEntity = persistedEntity(alternativeRequesterId);
        when(getMockDao().loadById(existingEntity.getId())).thenReturn(existingEntity);

        assertThrows(AuthorizationException.class, () -> getService().delete(existingEntity.getId(), requesterId));
        verify(getMockDao(), never()).delete(any());
    }

    private Domain persistedEntity(UUID ownerId) {
        return buildPersistedEntity(UUID.randomUUID(), ownerId);
    }

    protected abstract Service getService();

    protected abstract Dao getMockDao();

    protected abstract NewDomain createValidNewDomain();

    protected abstract UpdateDomain createUpdateDomain();

    /**
     * Domain identity is immutable once assigned (see AbstractCreatable) - there is no
     * setId(). A real DAO returns a new persisted instance rather than mutating the
     * transient one it was given, and the mock must model that honestly rather than
     * faking mutation on an object that structurally cannot be mutated.
     * <p>
     * Used exclusively by the create() ownership-stamping test - fixtures for
     * update/delete/loadById use buildPersistedEntity() instead, since those tests
     * shouldn't depend on create() working correctly.
     */
    protected abstract Domain withId(Domain transientDomain, UUID id);

    /**
     * Builds an already-persisted domain instance directly, without routing through
     * the service's create() path. Fixture setup should not depend on production
     * code under test elsewhere in the suite.
     */
    protected abstract Domain buildPersistedEntity(UUID id, UUID ownerId);
}