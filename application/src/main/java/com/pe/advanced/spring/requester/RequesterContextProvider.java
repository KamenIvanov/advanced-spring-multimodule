package com.pe.advanced.spring.requester;

import com.pe.advanced.api.rest.HeaderConstants;
import com.pe.advanced.domain.exceptions.AuthenticationException;
import com.pe.advanced.domain.requester.CurrentRequesterProvider;
import com.pe.advanced.domain.requester.Requester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

/**
 * Gateway-provided requester headers, available only within the current HTTP request.
 * <p>
 * A missing identification and an unreadable one are the same answer to the caller, because
 * telling it which of the two happened says something about what the service accepts. They are
 * not the same answer in the log, which is the only place the difference is useful.
 */
public class RequesterContextProvider implements CurrentRequesterProvider {

    private static final Logger logger = LoggerFactory.getLogger(RequesterContextProvider.class);

    private static final String UNAUTHENTICATED = "Client is not authenticated.";

    @Override
    public Requester getCurrent() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            throw new IllegalStateException("There is no current request");
        }

        final var request = attributes.getRequest();
        return new Requester(
                id(request.getHeader(HeaderConstants.REQUESTER_ID))
        );
    }

    private static UUID id(String header) {
        if (header == null || header.isBlank()) {
            logger.warn("Request arrived without a requester id");
            throw new AuthenticationException(UNAUTHENTICATED);
        }
        try {
            return UUID.fromString(header.trim());
        } catch (IllegalArgumentException e) {
            logger.warn("The requester's id {} isn't a UUID", header);
            throw new AuthenticationException(UNAUTHENTICATED);
        }
    }
}
