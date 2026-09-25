package com.pe.advanced.spring.exception;

import com.pe.advanced.domain.exceptions.AuthorizationException;
import com.pe.advanced.domain.exceptions.ConflictException;
import com.pe.advanced.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GeneralExceptionHandlerTestCase {

    private MockMvc mockMvc;

    @BeforeEach
    void setUpMockMvc() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new ThrowingController())
                .setControllerAdvice(new GeneralExceptionHandler())
                .build();
    }

    @Test
    void whenAuthorizationExceptionIsThrown_ShouldReturn403WithProblemDetail() throws Exception {
        mockMvc.perform(get("/test/authorization"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.detail").value("Client is not authorized for this operation."));
    }

    @Test
    void whenNotFoundExceptionIsThrown_ShouldReturn404WithProblemDetail() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("No such entity."));
    }

    @Test
    void whenIllegalStateExceptionIsThrown_ShouldReturn409WithProblemDetail() throws Exception {
        mockMvc.perform(get("/test/illegal-state"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.detail").value("Cannot transition from ARCHIVED to ACTIVE"));
    }

    @Test
    void whenConflictExceptionIsThrown_ShouldReturn409WithProblemDetail() throws Exception {
        mockMvc.perform(get("/test/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.detail").value("A category with that name already exists."));
    }

    @Test
    void whenUnexpectedExceptionIsThrown_ShouldReturn500WithGenericDetail() throws Exception {
        mockMvc.perform(get("/test/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred."));
    }

    @Test
    void whenUnexpectedExceptionIsThrown_ShouldNotLeakTheOriginalMessage() throws Exception {
        final var response = mockMvc.perform(get("/test/unexpected"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // The raw message of an unanticipated failure must never reach the client -
        // it may carry internal details never written with an external reader in mind.
        assertFalse(response.contains("Connection refused to internal-db-host:3306"));
    }

    @Test
    void whenRequesterIdIsMalformed_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/test/requester").param("X-Requester-Id", "not-a-uuid"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.detail").value("Client is not authenticated."));
    }

    @Test
    void whenAnotherArgumentIsMalformed_ShouldReturn400() throws Exception {
        mockMvc.perform(get("/test/product/{id}", "not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Malformed value for 'id'."));
    }

    @Test
    void whenRequestBodyIsUnreadable_ShouldReturn400() throws Exception {
        mockMvc.perform(post("/test/body")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ this is not json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Request body could not be read."));
    }

    @RestController
    @RequestMapping("/test")
    static class ThrowingController {

        @GetMapping("/authorization")
        void authorization() {
            throw new AuthorizationException("Client is not authorized for this operation.");
        }

        @GetMapping("/not-found")
        void notFound() {
            throw new NotFoundException("No such entity.");
        }

        @GetMapping("/illegal-state")
        void illegalState() {
            throw new IllegalStateException("Cannot transition from ARCHIVED to ACTIVE");
        }

        @GetMapping("/conflict")
        void conflict() {
            throw new ConflictException("A category with that name already exists.");
        }

        @GetMapping("/unexpected")
        void unexpected() {
            throw new RuntimeException("Connection refused to internal-db-host:3306");
        }

        @GetMapping("/requester")
        void requester(@RequestParam(GeneralExceptionHandler.HEADER_REQUESTER_ID) UUID requesterId) {
            // Never reached - the conversion fails first
        }

        @GetMapping("/product/{id}")
        void product(@PathVariable("id") UUID id) {
            // Never reached - the conversion fails first
        }

        @PostMapping("/body")
        void body(@RequestBody Map<String, Object> payload) {
            // Never reached - parsing fails first
        }
    }
}
