package com.pe.advanced.bl.service;

import com.pe.advanced.domain.AbstractAuditable;

import java.util.UUID;

public interface CrudService<CreateDomain, UpdateDomain, Domain extends AbstractAuditable<UUID>> {

    Domain create(CreateDomain newDomain, UUID requesterId);

    /**
     * @param expectedVersion the version the caller read, or null to write without checking that
     *                        the stored row has not moved since
     */
    Domain update(UUID id, UpdateDomain updateDomain, UUID requesterId, Integer expectedVersion);

    Domain loadById(UUID id, UUID requesterId);

    void delete(UUID id, UUID requesterId);
}
