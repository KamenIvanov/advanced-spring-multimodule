package com.pe.advanced.bl.service.impl;

import com.pe.advanced.domain.AbstractUpdatable;
import com.pe.advanced.domain.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.function.Supplier;

public abstract class AbstractService {

    protected static final String MISSING_ENTITY = "No such entity.";
    protected static final String UNAUTHORIZED   = "Client is not authorized for this operation.";

    protected static final Logger logger = LoggerFactory.getLogger(AbstractService.class);

    protected <Domain extends AbstractUpdatable<UUID>> Domain loadOrThrowNotFound(Supplier<Domain> domainSupplier) {
        final Domain entity = domainSupplier.get();
        if (entity == null) {
            throw new NotFoundException(MISSING_ENTITY);
        }
        return entity;
    }
}
