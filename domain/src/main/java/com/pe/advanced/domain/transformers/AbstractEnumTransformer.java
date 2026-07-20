package com.pe.advanced.domain.transformers;

import com.pe.advanced.domain.utils.ClassUtils;

public abstract class AbstractEnumTransformer<Input extends Enum<Input>, Output extends Enum<Output>> implements BiTransformer<Input, Output> {

    private final Class<Input>   entityClass;
    private final Class<Output> entityVoClass;

    protected AbstractEnumTransformer(Class<Input> entityClass, Class<Output> entityVoClass) {
        this.entityClass = entityClass;
        this.entityVoClass = entityVoClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Input createInput(Output output) {
        if (output == null) {
            return null;
        }
        return (Input) ClassUtils.enumForName(entityClass, output.name());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Output createOutput(Input entity) {
        if (entity == null) {
            return null;
        }
        return (Output) ClassUtils.enumForName(entityVoClass, entity.name());
    }

}
