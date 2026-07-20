package com.pe.advanced.domain.asserter;

import com.pe.advanced.domain.AbstractCreatable;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractCreatableDeepEqualsAsserter<Domain extends AbstractCreatable<UUID>> implements DeepEqualsAsserter<Domain> {

    @Override
    public void assertDeepEquals(Domain expected, Domain actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCreatedAt().truncatedTo(ChronoUnit.SECONDS), actual.getCreatedAt().truncatedTo(ChronoUnit.SECONDS));
    }
}

