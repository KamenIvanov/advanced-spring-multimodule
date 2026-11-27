package com.pe.advanced.dao.api;

import com.pe.advanced.domain.AbstractCreatable;

public interface CrudDao<IdType, Type extends AbstractCreatable<IdType>> {

    /**
     * Creates the entity in the database.
     *
     * @param e the entity being persisted
     */
    Type create(Type e);

    /**
     * Updates the entity in the database.
     *
     * @param e the entity being persisted
     */
    Type update(Type e);

    /**
     * Updates the entity, refusing the write when the stored row has moved on since the caller
     * read it.
     *
     * @param e               the entity being persisted
     * @param expectedVersion the version the caller read, or null to write without checking
     * @throws com.pe.advanced.domain.exceptions.ConflictException when the stored version differs
     */
    Type update(Type e, Integer expectedVersion);

    /**
     * Loads an entity from the database by the given id, or null if not found.
     *
     * @param id the entity's id
     * @return the loaded entity
     */
    Type loadById(IdType id);

    /**
     * @param e the entity's id being deleted
     */
    void delete(Type e);
}
