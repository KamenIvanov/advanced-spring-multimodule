package com.pe.advanced.domain.transformers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface BiTransformer<Input, Output> extends Transformer<Input, Output> {

    Input createInput(Output output);

    default void copyToInput(Output output, Input input) {

    }

    default List<Input> createInput(List<Output> entityVos) {
        if (entityVos == null) {
            return null;
        }
        return entityVos
                .stream()
                .map(this::createInput)
                .collect(Collectors.toList());
    }

    default Set<Input> createInput(Set<Output> entityVos) {
        if (entityVos == null) {
            return null;
        }
        return entityVos
                .stream()
                .map(this::createInput)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
