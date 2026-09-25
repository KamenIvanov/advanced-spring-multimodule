package com.pe.advanced.spring.exception;

import com.pe.advanced.domain.exceptions.AuthorizationException;
import com.pe.advanced.domain.exceptions.ConflictException;
import com.pe.advanced.domain.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    /**
     * A malformed path variable or request parameter is a bad request - except when the
     * parameter in question is the requester id, which means the caller never identified
     * itself at all. That is an authentication failure, not a malformed argument.
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public final ProblemDetail handleException(MethodArgumentTypeMismatchException e) {
        if ("X-Requester-Id".equals(e.getName())) {
            logger.warn("Request arrived without a usable requester id");
            return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Client is not authenticated.");
        }

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