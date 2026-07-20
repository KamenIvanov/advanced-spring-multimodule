package com.pe.advanced.dao.impl.transformers;

import com.pe.advanced.dao.impl.AbstractCreatableEntity;
import com.pe.advanced.domain.AbstractCreatable;
import com.pe.advanced.domain.transformers.BiTransformer;

import java.util.UUID;

public abstract class AbstractCreatableEntityTransformer<S extends AbstractCreatableEntity, D extends AbstractCreatable<UUID>> implements BiTransformer<S, D> {

    @Override
    public void copyToInput(D dest, S source) {
        source.setId(dest.getId());
        source.setCreatedAt(dest.getCreatedAt());
    }

    @Override
    public void copyToOutput(S source, D dest) {
        dest.setId(source.getId());
        dest.setCreatedAt(source.getCreatedAt());
    }
}
