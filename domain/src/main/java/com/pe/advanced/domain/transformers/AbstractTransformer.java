package com.pe.advanced.domain.transformers;

public abstract class AbstractTransformer<Input, Output> implements Transformer<Input, Output> {

    @Override
    public Output createOutput(Input input) {
        throw new IllegalArgumentException("Not implemented yet");
    }
}
