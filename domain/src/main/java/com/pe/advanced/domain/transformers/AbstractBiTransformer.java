package com.pe.advanced.domain.transformers;

public abstract class AbstractBiTransformer<Input, Output> implements BiTransformer<Input, Output> {

    @Override
    public Input createInput(Output output) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @Override
    public Output createOutput(Input input) {
        throw new IllegalArgumentException("Not implemented yet");
    }
}
