package com.pe.advanced.domain.transformers;

import com.pe.advanced.domain.utils.EnumUtils;

public abstract class AbstractEnumTransformer<Input extends Enum<Input>, Output extends Enum<Output>> implements BiTransformer<Input, Output> {

    private final Class<Input> inputClass;
    private final Class<Output> outputClass;

    protected AbstractEnumTransformer(Class<Input> inputClass, Class<Output> outputClass) {
        this.inputClass = inputClass;
        this.outputClass = outputClass;
    }

    @Override
    public Input createInput(Output output) {
        if (output == null) {
            return null;
        }
        return EnumUtils.enumForName(inputClass, output.name());
    }

    @Override
    public Output createOutput(Input entity) {
        if (entity == null) {
            return null;
        }
        return EnumUtils.enumForName(outputClass, entity.name());
    }
}
