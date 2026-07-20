package com.pe.advanced.domain.asserter;

import com.pe.advanced.domain.AbstractAuditable;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractAuditableDeepEqualsAsserter<Domain extends AbstractAuditable<UUID>> extends AbstractUpdatableDeepEqualsAsserter<Domain> {

    @Override
    public void assertDeepEquals(Domain expected, Domain actual) {
        super.assertDeepEquals(expected, actual);
        assertEquals(expected.getCreatedById(), actual.getCreatedById());
        assertEquals(expected.getUpdatedById(), actual.getUpdatedById());
    }
}

