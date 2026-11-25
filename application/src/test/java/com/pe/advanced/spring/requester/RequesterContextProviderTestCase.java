package com.pe.advanced.spring.requester;

import com.pe.advanced.api.rest.HeaderConstants;
import com.pe.advanced.domain.exceptions.AuthenticationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The provider has three outcomes, and the two failing ones are deliberately the same type with
 * different log lines. A caller that cannot be identified learns nothing about which header the
 * service wanted or what shape it should have had.
 */
class RequesterContextProviderTestCase {

    private final RequesterContextProvider provider = new RequesterContextProvider();

    @AfterEach
    void clearRequest() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void readsTheIdentificationFromTheHeader() {
        final var id = UUID.randomUUID();
        request(id.toString());

        assertEquals(id, provider.getCurrent().id());
    }

    @Test
    void refusesARequestWithoutAnIdentification() {
        request(null);

        assertThrows(AuthenticationException.class, provider::getCurrent);
    }

    @Test
    void refusesARequestWhoseIdentificationIsBlank() {
        request("   ");

        assertThrows(AuthenticationException.class, provider::getCurrent);
    }

    @Test
    void refusesAnIdentificationThatIsNotAUuid() {
        request("not-a-uuid");

        assertThrows(AuthenticationException.class, provider::getCurrent);
    }

    /**
     * No request at all is a programming error rather than a client's: something behind the web
     * layer asked who was calling when nobody was. It must not look like a failed login.
     */
    @Test
    void refusesToAnswerOutsideARequest() {
        assertThrows(IllegalStateException.class, provider::getCurrent);
    }

    private static void request(String requesterId) {
        final var request = new MockHttpServletRequest();
        if (requesterId != null) {
            request.addHeader(HeaderConstants.REQUESTER_ID, requesterId);
        }
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }
}
