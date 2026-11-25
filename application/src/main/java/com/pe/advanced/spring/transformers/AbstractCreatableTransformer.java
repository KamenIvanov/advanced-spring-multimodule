package com.pe.advanced.spring.transformers;

import com.pe.advanced.api.dto.AbstractCreatableEntityDto;
import com.pe.advanced.domain.AbstractCreatable;
import com.pe.advanced.domain.transformers.AbstractTransformer;

import java.util.UUID;

public abstract class AbstractCreatableTransformer<Input extends AbstractCreatable<UUID>, Output extends AbstractCreatableEntityDto> extends AbstractTransformer<Input, Output> {

    @Override
    public void copyToOutput(Input input, Output output) {
        output.setId(input.getId());
        output.setCreatedAt(input.getCreatedAt());
    }
}
