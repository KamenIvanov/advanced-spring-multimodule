package com.pe.advanced.domain.exceptions;

/**
 * The caller could not be identified at all: no identification was supplied, or what was
 * supplied is unusable.
 * <p>
 * Distinct from {@link AuthorizationException}, which means the caller is known and the
 * operation is not theirs to perform. The two differ in what the caller can do about it:
 * identifying itself again can resolve this one and will never resolve the other.
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
