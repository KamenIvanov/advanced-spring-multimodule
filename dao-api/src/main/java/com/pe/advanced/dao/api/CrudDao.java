package com.pe.advanced.dao.api;

public interface CrudDao<IdType, Type> {

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
