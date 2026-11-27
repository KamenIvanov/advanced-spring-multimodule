package com.pe.advanced.domain.exceptions;

/**
 * The request was understood and is not acceptable: a value outside the range the contract
 * documents, or a required precondition that is missing or unreadable.
 * <p>
 * The third kind of failure, next to {@link NotFoundException} and {@link ConflictException}. It
 * is not an authorization problem, because the caller may well be allowed to perform the
 * operation, and it is not a conflict, because nothing in the stored data disagrees with it yet.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
