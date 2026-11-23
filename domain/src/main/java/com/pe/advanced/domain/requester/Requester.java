package com.pe.advanced.domain.requester;

import java.util.Objects;
import java.util.UUID;

/**
 * Who makes the current request. Only the identification for now; an email, a locale or a
 * session would be added here rather than passed alongside, which is the point of the type.
 */
public record Requester(UUID id) {

    public Requester {
        Objects.requireNonNull(id, "The requester's id is mandatory");
    }
}