package com.pe.advanced.domain.asserter;

import com.pe.advanced.domain.AbstractUpdatable;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractUpdatableDeepEqualsAsserter<Domain extends AbstractUpdatable<UUID>> extends AbstractCreatableDeepEqualsAsserter<Domain> {

    @Override
    public void assertDeepEquals(Domain expected, Domain actual) {
        super.assertDeepEquals(expected, actual);
        assertEquals(expected.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS), actual.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS));
    }
}

