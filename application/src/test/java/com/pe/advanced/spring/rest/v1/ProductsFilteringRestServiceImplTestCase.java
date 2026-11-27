package com.pe.advanced.spring.rest.v1;

import com.pe.advanced.api.json.JsonMapperSingleton;
import com.pe.advanced.api.rest.HeaderConstants;
import com.pe.advanced.api.rest.RestUrl;
import com.pe.advanced.bl.service.ProductsService;
import com.pe.advanced.domain.exceptions.NotFoundException;
import com.pe.advanced.domain.product.Product;
import com.pe.advanced.domain.product.ProductStatus;
import com.pe.advanced.spring.exception.GeneralExceptionHandler;
import com.pe.advanced.spring.rest.v1.pub.ProductsFilteringRestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The anonymous catalogue. Two things are being asserted that the secured resource cannot:
 * that this one answers without any identification at all, and that what it returns is the
 * narrower projection, carrying nothing a reader has no business seeing.
 */
class ProductsFilteringRestServiceImplTestCase {

    private ProductsService productsService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.productsService = mock(ProductsService.class);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new ProductsFilteringRestServiceImpl(productsService))
                .setMessageConverters(new JacksonJsonHttpMessageConverter(JsonMapperSingleton.instance.getJsonMapper()))
                .setControllerAdvice(new GeneralExceptionHandler())
                .build();
    }

    @Test
    void answersWithoutAnyIdentification() throws Exception {
        final var id = UUID.randomUUID();
        when(productsService.loadPublicById(id)).thenReturn(product(id));

        mockMvc.perform(get(RestUrl.PRODUCTS + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("A product"));
    }

    /**
     * The public projection is a different type rather than the same one with fields omitted, so
     * these absences are structural. The test exists because the two types are easy to confuse at
     * the transformer, and confusing them publishes an audit trail and a concurrency token to
     * anybody who asks.
     */
    @Test
    void doesNotPublishTheConcurrencyTokenOrTheUpdateAudit() throws Exception {
        final var id = UUID.randomUUID();
        when(productsService.loadPublicById(id)).thenReturn(product(id));

        mockMvc.perform(get(RestUrl.PRODUCTS + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").doesNotExist())
                .andExpect(jsonPath("$.updatedAt").doesNotExist())
                .andExpect(jsonPath("$.specification").doesNotExist())
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void ignoresAnIdentificationIfOneIsSentAnyway() throws Exception {
        final var id = UUID.randomUUID();
        when(productsService.loadPublicById(id)).thenReturn(product(id));

        mockMvc.perform(get(RestUrl.PRODUCTS + "/{id}", id)
                        .header(HeaderConstants.REQUESTER_ID, UUID.randomUUID()))
                .andExpect(status().isOk());
    }

    @Test
    void answersNotFoundForAnUnknownIdentification() throws Exception {
        final var id = UUID.randomUUID();
        when(productsService.loadPublicById(id)).thenThrow(new NotFoundException("No such entity."));

        mockMvc.perform(get(RestUrl.PRODUCTS + "/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void refusesAPageBelowTheFirstOne() throws Exception {
        mockMvc.perform(get(RestUrl.PRODUCTS).param("page", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    private static Product product(UUID id) {
        final var createdAt = Instant.parse("2026-01-02T03:04:05.006Z");
        final var product = new Product(id, createdAt, createdAt, ProductStatus.ACTIVE);
        product.setName("A product");
        product.setSku("SKU-1");
        product.setPrice(new BigDecimal("19.90"));
        product.setCreatedById(UUID.randomUUID());
        return product;
    }
}
