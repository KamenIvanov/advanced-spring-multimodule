package com.pe.advanced.domain.transformers;

import com.pe.advanced.domain.utils.EnumUtils;

public abstract class AbstractEnumTransformer<Input extends Enum<Input>, Output extends Enum<Output>> implements BiTransformer<Input, Output> {

    private final Class<Input> entityClass;
    private final Class<Output> entityVoClass;

    protected AbstractEnumTransformer(Class<Input> entityClass, Class<Output> entityVoClass) {
        this.entityClass = entityClass;
        this.entityVoClass = entityVoClass;
    }

    @Override
    public Input createInput(Output output) {
        if (output == null) {
            return null;
        }
        return EnumUtils.enumForName(entityClass, output.name());
    }

    @Override
    public Output createOutput(Input entity) {
        if (entity == null) {
            return null;
        }
        return EnumUtils.enumForName(entityVoClass, entity.name());
    }
}
