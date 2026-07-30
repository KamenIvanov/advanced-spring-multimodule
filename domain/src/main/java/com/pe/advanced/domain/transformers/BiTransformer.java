package com.pe.advanced.domain.transformers;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface BiTransformer<Input, Output> extends Transformer<Input, Output> {

    /**
     * Creates a new instance of an Input type, with transformed data from the output object.
     *
     * @param output the output object
     * @return the newly created Input object
     */
    Input createInput(Output output);

    /**
     * Copies the data from output to input.
     *
     * @param output the output entity
     * @param input  the input entity
     */
    default void copyToInput(Output output, Input input) {

    }

    default List<Input> createInput(List<Output> entityVos) {
        if (entityVos == null) {
            return Collections.emptyList();
        }
        return entityVos
                .stream()
                .map(this::createInput)
                .collect(Collectors.toList());
    }

    default Set<Input> createInput(Set<Output> entityVos) {
        if (entityVos == null) {
            return Collections.emptySet();
        }
        return entityVos
                .stream()
                .map(this::createInput)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
