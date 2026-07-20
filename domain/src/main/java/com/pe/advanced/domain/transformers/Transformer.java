package com.pe.advanced.domain.transformers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface Transformer<Input, Output> {

    Output createOutput(Input input);

    /**
     * Copy the data from entityVo to entity.
     *
     * @param input  the input entity
     * @param output the output entity
     */
    default void copyToOutput(Input input, Output output) {

    }

    default List<Output> createOutput(List<Input> inputs) {
        if (inputs == null) {
            return null;
        }
        return inputs
                .stream()
                .map(this::createOutput)
                .collect(Collectors.toList());
    }

    default Set<Output> createOutput(Set<Input> inputs) {
        if (inputs == null) {
            return null;
        }
        return inputs
                .stream()
                .map(this::createOutput)
                .collect(Collectors.toCollection(HashSet::new));
    }
}

