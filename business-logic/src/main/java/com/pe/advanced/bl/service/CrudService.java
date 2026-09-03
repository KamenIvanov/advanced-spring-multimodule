package com.pe.advanced.bl.service;

import com.pe.advanced.domain.AbstractAuditable;

import java.util.UUID;

public interface CrudService<CreateDomain, UpdateDomain, Domain extends AbstractAuditable<UUID>> {

    Domain create(CreateDomain newDomain, UUID requesterId);

    Domain update(UUID id, UpdateDomain updateDomain, UUID requesterId);

    Domain loadById(UUID id, UUID requesterId);

    void delete(UUID id, UUID requesterId);
}
