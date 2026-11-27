package com.pe.advanced.spring.exception;

import com.pe.advanced.domain.exceptions.AuthenticationException;
import com.pe.advanced.domain.exceptions.AuthorizationException;
import com.pe.advanced.domain.exceptions.ConflictException;
import com.pe.advanced.domain.exceptions.NotFoundException;
import com.pe.advanced.domain.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    /**
     * A malformed path variable or request parameter is a bad request. The requester's
     * identification is no longer among them: it is a header read by the provider, which
     * reports its own failure as an authentication one.
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public final ProblemDetail handleException(MethodArgumentTypeMismatchException e) {
        logger.warn("Malformed argument '{}': {}", e.getName(), e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Malformed value for '" + e.getName() + "'.");
    }

    /**
     * The request body could not be parsed at all - this fails before any domain object
     * exists to validate, which is why it is handled here rather than in the service layer.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.warn("Unreadable request body: {}", e.getMessage());
        final var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Request body could not be read.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public final ProblemDetail handleException(DataIntegrityViolationException e) {
        logger.warn("Integrity constraint violated: {}", e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "The request conflicts with existing data.");
    }

    /**
     * A body that broke the constraints the contract declares. The fields are named, because a
     * client developer reading "validation failed" has to guess, and the generated specification
     * already tells them which fields are required - this says which ones they missed.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        final var fields = e.getBindingResult().getFieldErrors().stream()
                .map(error -> "'" + error.getField() + "' " + error.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining(", "));

        logger.warn("Rejected body: {}", fields);

        final var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, fields);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler({ValidationException.class})
    public final ProblemDetail handleException(ValidationException e) {
        logger.warn("Rejected request: {}", e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * Hibernate's own optimistic locking, as opposed to the precondition the DAO checks against
     * what the caller read. This one means two writes raced inside the same moment, which is rare
     * and which the caller resolves the same way: read again and retry.
     */
    @ExceptionHandler({ObjectOptimisticLockingFailureException.class})
    public final ProblemDetail handleException(ObjectOptimisticLockingFailureException e) {
        logger.warn("Lost optimistic locking race: {}", e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "The entity was changed by somebody else since it was read. Read it again and retry.");
    }

    @ExceptionHandler({AuthenticationException.class})
    public final ProblemDetail handleException(AuthenticationException e) {
        logger.warn("Unauthenticated request: {}", e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler({AuthorizationException.class})
    public final ProblemDetail handleException(AuthorizationException e) {
        logger.warn("Authorization failed: {}", e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler({NotFoundException.class})
    public final ProblemDetail handleException(NotFoundException e) {
        logger.warn("Entity not found: {}", e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({IllegalStateException.class})
    public final ProblemDetail handleException(IllegalStateException e) {
        logger.warn("Invalid state transition: {}", e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler({ConflictException.class})
    public final ProblemDetail handleException(ConflictException e) {
        logger.warn("Conflict: {}", e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler({Throwable.class})
    public final ProblemDetail handleException(Throwable e) {
        logger.error("Unhandled exception", e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
    }
}