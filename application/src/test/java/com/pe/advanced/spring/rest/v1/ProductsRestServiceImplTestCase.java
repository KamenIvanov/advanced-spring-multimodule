package com.pe.advanced.spring.rest.v1;

import com.pe.advanced.api.json.JsonMapperSingleton;
import com.pe.advanced.api.rest.HeaderConstants;
import com.pe.advanced.api.rest.RestUrl;
import com.pe.advanced.bl.service.ProductsService;
import com.pe.advanced.dao.api.product.ProductSort;
import com.pe.advanced.dao.api.search.ResultPage;
import com.pe.advanced.dao.api.search.SortDirection;
import com.pe.advanced.domain.exceptions.AuthorizationException;
import com.pe.advanced.domain.exceptions.ConflictException;
import com.pe.advanced.domain.exceptions.NotFoundException;
import com.pe.advanced.domain.product.Product;
import com.pe.advanced.domain.product.ProductStatus;
import com.pe.advanced.spring.exception.GeneralExceptionHandler;
import com.pe.advanced.spring.requester.RequesterContextProvider;
import com.pe.advanced.spring.rest.v1.secured.ProductsRestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The secured product resource, exercised through the real request pipeline rather than by
 * calling the controller's methods.
 * <p>
 * {@code standaloneSetup} rather than {@code @WebMvcTest}: nothing in this application is
 * component scanned, so a slice has no scanned candidates to filter and would instead import the
 * whole configuration chain, down to a datasource. What matters here is reachable without a
 * context, as long as the message converter is built from the same mapper the application
 * declares as a bean - otherwise these tests would prove a serialization nobody performs.
 */
class ProductsRestServiceImplTestCase {

    private static final UUID REQUESTER = UUID.randomUUID();

    /** Complete on purpose: an incomplete one has its own test below. */
    private static final String BODY =
            "{\"name\":\"A\",\"sku\":\"B\",\"price\":\"1.00\",\"dimensions\":\"10x10x10cm\",\"weight\":450}";

    private ProductsService productsService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.productsService = mock(ProductsService.class);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new ProductsRestServiceImpl(productsService, new RequesterContextProvider()))
                .setMessageConverters(new JacksonJsonHttpMessageConverter(JsonMapperSingleton.instance.getJsonMapper()))
                .setControllerAdvice(new GeneralExceptionHandler())
                .build();
    }

    /**
     * The one assertion that proves the contract's mapper is what the pipeline uses. A price
     * written as a JSON number would be a document-conformant response and a broken one.
     */
    @Test
    void writesThePriceAsAStringAndTheInstantInThePinnedFormat() throws Exception {
        final var product = product(UUID.randomUUID(), REQUESTER);
        when(productsService.loadById(product.getId(), REQUESTER)).thenReturn(product);

        mockMvc.perform(get(RestUrl.SECURED_PRODUCTS + "/{id}", product.getId())
                        .header(HeaderConstants.REQUESTER_ID, REQUESTER))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("\"price\":\"19.90\"")))
                .andExpect(jsonPath("$.createdAt").value("2026-01-02T03:04:05.006Z"));
    }

    @Test
    void refusesARequestWithoutAnIdentification() throws Exception {
        mockMvc.perform(get(RestUrl.SECURED_PRODUCTS + "/{id}", UUID.randomUUID()))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void refusesAnIdentificationThatIsNotAUuid() throws Exception {
        mockMvc.perform(get(RestUrl.SECURED_PRODUCTS + "/{id}", UUID.randomUUID())
                        .header(HeaderConstants.REQUESTER_ID, "not-a-uuid"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    /**
     * Not identified and not permitted are different answers, and the difference is what the
     * caller can do about it: identifying itself again resolves one and never the other.
     */
    @Test
    void answersForbiddenForSomebodyElsesProduct() throws Exception {
        final var id = UUID.randomUUID();
        when(productsService.loadById(id, REQUESTER)).thenThrow(new AuthorizationException("Client is not authorized for this operation."));

        mockMvc.perform(get(RestUrl.SECURED_PRODUCTS + "/{id}", id)
                        .header(HeaderConstants.REQUESTER_ID, REQUESTER))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void answersNotFoundForAnUnknownIdentification() throws Exception {
        final var id = UUID.randomUUID();
        when(productsService.loadById(id, REQUESTER)).thenThrow(new NotFoundException("No such entity."));

        mockMvc.perform(get(RestUrl.SECURED_PRODUCTS + "/{id}", id)
                        .header(HeaderConstants.REQUESTER_ID, REQUESTER))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void refusesAWriteWithoutThePrecondition() throws Exception {
        mockMvc.perform(put(RestUrl.SECURED_PRODUCTS + "/{id}", UUID.randomUUID())
                        .header(HeaderConstants.REQUESTER_ID, REQUESTER)
                        .contentType("application/json")
                        .content(BODY))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void refusesAPreconditionThatIsNotAVersion() throws Exception {
        mockMvc.perform(put(RestUrl.SECURED_PRODUCTS + "/{id}", UUID.randomUUID())
                        .header(HeaderConstants.REQUESTER_ID, REQUESTER)
                        .header(HeaderConstants.IF_MATCH, "yesterday")
                        .contentType("application/json")
                        .content(BODY))
                .andExpect(status().isBadRequest());
    }

    @Test
    void passesThePreconditionToTheServiceAndAnswersConflictWhenItIsStale() throws Exception {
        final var id = UUID.randomUUID();
        when(productsService.update(eq(id), any(), eq(REQUESTER), eq(7)))
                .thenThrow(new ConflictException("The entity was changed by somebody else since it was read. Read it again and retry."));

        mockMvc.perform(put(RestUrl.SECURED_PRODUCTS + "/{id}", id)
                        .header(HeaderConstants.REQUESTER_ID, REQUESTER)
                        .header(HeaderConstants.IF_MATCH, "7")
                        .contentType("application/json")
                        .content(BODY))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    /**
     * The contract numbers pages from one. A caller asking for page one must not be answered with
     * the second page, and a caller counting from zero must be told rather than quietly served.
     */
    @Test
    void translatesTheFirstPageToTheFirstIndex() throws Exception {
        when(productsService.getOwnProducts(anyInt(), anyInt(), any(), any(), any(), eq(REQUESTER)))
                .thenReturn(new ResultPage<>(0, 0L, List.of()));

        mockMvc.perform(get(RestUrl.SECURED_PRODUCTS)
                        .param("page", "1")
                        .param("size", "20")
                        .header(HeaderConstants.REQUESTER_ID, REQUESTER))
                .andExpect(status().isOk());

        final var page = ArgumentCaptor.forClass(Integer.class);
        final var size = ArgumentCaptor.forClass(Integer.class);
        verify(productsService).getOwnProducts(page.capture(), size.capture(),
                any(ProductSort.class), any(SortDirection.class), any(), eq(REQUESTER));

        assertEquals(0, page.getValue());
        assertEquals(20, size.getValue());
    }

    @Test
    void refusesAPageBelowTheFirstOne() throws Exception {
        mockMvc.perform(get(RestUrl.SECURED_PRODUCTS)
                        .param("page", "0")
                        .header(HeaderConstants.REQUESTER_ID, REQUESTER))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void refusesAPageLargerThanTheContractAllows() throws Exception {
        mockMvc.perform(get(RestUrl.SECURED_PRODUCTS)
                        .param("size", "101")
                        .header(HeaderConstants.REQUESTER_ID, REQUESTER))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    /**
     * The domain holds the weight as a primitive, so a body without one would be unboxed from
     * null and answer 500. The contract declares the field required, so the rejection happens
     * before any of this code runs, and the response names the field rather than the failure.
     */
    @Test
    void refusesABodyWithoutTheWeightTheContractRequires() throws Exception {
        mockMvc.perform(put(RestUrl.SECURED_PRODUCTS + "/{id}", UUID.randomUUID())
                        .header(HeaderConstants.REQUESTER_ID, REQUESTER)
                        .header(HeaderConstants.IF_MATCH, "1")
                        .contentType("application/json")
                        .content("{\"name\":\"A\",\"sku\":\"B\",\"price\":\"1.00\",\"dimensions\":\"10x10x10cm\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString("'weight'")));
    }

    private static Product product(UUID id, UUID owner) {
        final var createdAt = Instant.parse("2026-01-02T03:04:05.006Z");
        final var product = new Product(id, createdAt, createdAt, ProductStatus.ACTIVE);
        product.setName("A product");
        product.setSku("SKU-1");
        product.setPrice(new BigDecimal("19.90"));
        product.setCreatedById(owner);
        return product;
    }
}
