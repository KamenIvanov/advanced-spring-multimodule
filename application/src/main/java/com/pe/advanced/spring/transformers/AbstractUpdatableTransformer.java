package com.pe.advanced.spring.transformers;

import com.pe.advanced.api.dto.AbstractEntityDto;
import com.pe.advanced.domain.AbstractUpdatable;

import java.util.UUID;

public abstract class AbstractUpdatableTransformer<Input extends AbstractUpdatable<UUID>, Output extends AbstractEntityDto> extends AbstractCreatableTransformer<Input, Output> {

    @Override
    public void copyToOutput(Input input, Output output) {
        super.copyToOutput(input, output);
        output.setUpdatedAt(input.getUpdatedAt());
    }
}
