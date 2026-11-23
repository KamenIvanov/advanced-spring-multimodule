package com.pe.advanced.spring.config;

import com.pe.advanced.api.json.JsonMapperSingleton;
import com.pe.advanced.api.rest.pub.product.ProductsFilteringRestService;
import com.pe.advanced.api.rest.secured.product.ProductsRestService;
import com.pe.advanced.bl.service.ProductsService;
import com.pe.advanced.domain.requester.CurrentRequesterProvider;
import com.pe.advanced.spring.exception.GeneralExceptionHandler;
import com.pe.advanced.spring.requester.RequesterContextProvider;
import com.pe.advanced.spring.rest.v1.pub.ProductsFilteringRestServiceImpl;
import com.pe.advanced.spring.rest.v1.secured.ProductsRestServiceImpl;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.json.JsonMapper;

/**
 * The HTTP surface, declared rather than scanned, for the reason Chapter 13 Part 1 gave: a list
 * that has to be edited is a list somebody reads.
 */
public class RestConfiguration {

    @Bean
    public EndpointsListener endpointsListener() {
        return EndpointsListener.instance;
    }

    @Bean
    public JsonMapper jsonMapper() {
        return JsonMapperSingleton.instance.getJsonMapper();
    }

    @Bean
    public GeneralExceptionHandler exceptionHandler() {
        return new GeneralExceptionHandler();
    }

    @Bean
    public CurrentRequesterProvider currentRequesterProvider() {
        return new RequesterContextProvider();
    }

    @Bean
    public ProductsRestService productsRestService(ProductsService productsService, CurrentRequesterProvider currentRequesterProvider) {
        return new ProductsRestServiceImpl(productsService, currentRequesterProvider);
    }

    @Bean
    public ProductsFilteringRestService productsFilteringRestService(ProductsService productsService) {
        return new ProductsFilteringRestServiceImpl(productsService);
    }
}
