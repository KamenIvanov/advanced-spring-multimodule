package com.pe.advanced.spring.rest.v1.precondition;

import com.pe.advanced.api.rest.HeaderConstants;
import com.pe.advanced.domain.exceptions.ValidationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * The version the caller read, taken from the request rather than from an argument.
 * <p>
 * The contract documents this header as required on every write, so an absent one is a bad
 * request and not a write without a precondition. Allowing it to be optional would mean the
 * safest callers are the ones that send it, which is the wrong way round.
 */
public final class IfMatch {

    private IfMatch() {
        // Static
    }

    public static int required() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            throw new IllegalStateException("There is no current request");
        }

        final var header = attributes.getRequest().getHeader(HeaderConstants.IF_MATCH);
        if (header == null || header.isBlank()) {
            throw new ValidationException("The '" + HeaderConstants.IF_MATCH + "' header is required, carrying the version the entity had when it was read.");
        }

        try {
            return Integer.parseInt(header.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("The '" + HeaderConstants.IF_MATCH + "' header must be the entity's version, which is a whole number.", e);
        }
    }
}
