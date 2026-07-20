package com.pe.advanced.dao.impl.transformers;

import com.pe.advanced.dao.impl.AbstractAuditableEntity;
import com.pe.advanced.domain.AbstractAuditable;

import java.util.UUID;

public abstract class AbstractAuditableEntityTransformer<S extends AbstractAuditableEntity, D extends AbstractAuditable<UUID>> extends AbstractUpdatableEntityTransformer<S, D> {

    @Override
    public void copyToInput(D dest, S source) {
        super.copyToInput(dest, source);

        source.setCreatedById(dest.getCreatedById());
        source.setUpdatedById(dest.getUpdatedById());
    }

    @Override
    public void copyToOutput(S source, D dest) {
        super.copyToOutput(source, dest);

        dest.setCreatedById(source.getCreatedById());
        dest.setUpdatedById(source.getUpdatedById());
    }
}
