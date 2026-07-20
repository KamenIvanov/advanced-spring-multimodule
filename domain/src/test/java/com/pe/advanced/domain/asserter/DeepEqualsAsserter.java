package com.pe.advanced.domain.asserter;

public interface DeepEqualsAsserter<T> {
    void assertDeepEquals(T expected, T actual);
}
