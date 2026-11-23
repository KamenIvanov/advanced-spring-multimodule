package com.pe.advanced.spring.rest.v1.pub;

import com.pe.advanced.api.dto.product.ProductDescriptionDto;
import com.pe.advanced.api.dto.product.ProductsDescriptionsDto;
import com.pe.advanced.api.rest.pub.product.ProductsFilteringRestService;
import com.pe.advanced.api.rest.search.ProductSearchParametersDto;
import com.pe.advanced.bl.service.ProductsService;
import com.pe.advanced.spring.transformers.product.ProductDescriptionTransformer;
import com.pe.advanced.spring.transformers.product.ProductsDescriptionsTransformer;
import com.pe.advanced.spring.rest.v1.search.PageArguments;
import com.pe.advanced.spring.transformers.search.ProductSortOptionTransformer;
import com.pe.advanced.spring.transformers.SortDirectionTransformer;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * The catalogue, with no requester anywhere in it. It does not extend
 * {@link com.pe.advanced.spring.rest.v1.AbstractRestServiceImpl} precisely because that base
 * class exists to resolve the current requester, and an anonymous resource that can reach one
 * is a resource somebody will eventually make depend on it.
 */
@RestController
public class ProductsFilteringRestServiceImpl implements ProductsFilteringRestService {

    private final ProductsService productsService;

    public ProductsFilteringRestServiceImpl(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Override
    public ProductsDescriptionsDto getProducts(ProductSearchParametersDto parameters) {
        final var page = productsService.getProducts(
                PageArguments.index(parameters.getPage()),
                PageArguments.size(parameters.getSize()),
                ProductSortOptionTransformer.instance.createInput(parameters.getSort()),
                SortDirectionTransformer.instance.createInput(parameters.getDirection()),
                parameters.getQuery()
        );
        return ProductsDescriptionsTransformer.instance.createOutput(page);
    }

    @Override
    public ProductDescriptionDto getProduct(UUID id) {
        final var product = productsService.loadPublicById(id);
        return ProductDescriptionTransformer.instance.createOutput(product);
    }
}
