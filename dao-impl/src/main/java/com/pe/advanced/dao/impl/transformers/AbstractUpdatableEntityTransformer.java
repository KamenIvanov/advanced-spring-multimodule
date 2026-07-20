package com.pe.advanced.dao.impl.transformers;

import com.pe.advanced.dao.impl.AbstractUpdatableEntity;
import com.pe.advanced.domain.AbstractUpdatable;

import java.util.UUID;

public abstract class AbstractUpdatableEntityTransformer<S extends AbstractUpdatableEntity, D extends AbstractUpdatable<UUID>> extends AbstractCreatableEntityTransformer<S, D> {

    @Override
    public void copyToInput(D dest, S source) {
        super.copyToInput(dest, source);

        source.setUpdatedAt(dest.getUpdatedAt());
    }

    @Override
    public void copyToOutput(S source, D dest) {
        super.copyToOutput(source, dest);

        dest.setUpdatedAt(source.getUpdatedAt());
    }
}
