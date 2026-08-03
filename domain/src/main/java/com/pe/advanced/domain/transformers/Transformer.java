package com.pe.advanced.domain.transformers;

import java.util.*;
import java.util.stream.Collectors;

public interface Transformer<Input, Output> {

    /**
     * Creates a new instance of an Output type, with transformed data from the input object.
     *
     * @param input the input object
     * @return the newly created Output object
     */
    Output createOutput(Input input);

    /**
     * Copies the data from input to output.
     *
     * @param input  the input object
     * @param output the output object
     */
    default void copyToOutput(Input input, Output output) {

    }

    default List<Output> createOutput(List<Input> inputs) {
        if (inputs == null) {
            return Collections.emptyList();
        }
        return inputs
                .stream()
                .map(this::createOutput)
                .collect(Collectors.toList());
    }

    default Set<Output> createOutput(Set<Input> inputs) {
        if (inputs == null) {
            return Collections.emptySet();
        }
        return inputs
                .stream()
                .map(this::createOutput)
                .collect(Collectors.toCollection(HashSet::new));
    }
}

