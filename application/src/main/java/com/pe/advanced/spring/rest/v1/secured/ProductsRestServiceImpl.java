package com.pe.advanced.spring.rest.v1.secured;

import com.pe.advanced.api.dto.product.NewProductDto;
import com.pe.advanced.api.dto.product.ProductDto;
import com.pe.advanced.api.dto.product.ProductStatusDto;
import com.pe.advanced.api.dto.product.ProductsDto;
import com.pe.advanced.api.dto.product.UpdateProductDto;
import com.pe.advanced.api.rest.search.ProductSearchParametersDto;
import com.pe.advanced.api.rest.secured.product.ProductsRestService;
import com.pe.advanced.bl.service.ProductsService;
import com.pe.advanced.domain.requester.CurrentRequesterProvider;
import com.pe.advanced.spring.rest.v1.AbstractRestServiceImpl;
import com.pe.advanced.spring.transformers.product.NewProductTransformer;
import com.pe.advanced.spring.transformers.product.ProductStatusTransformer;
import com.pe.advanced.spring.transformers.product.ProductTransformer;
import com.pe.advanced.spring.transformers.product.ProductsTransformer;
import com.pe.advanced.spring.transformers.product.UpdateProductTransformer;
import com.pe.advanced.spring.rest.v1.search.PageArguments;
import com.pe.advanced.spring.transformers.search.ProductSortOptionTransformer;
import com.pe.advanced.spring.transformers.SortDirectionTransformer;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * The mappings, the parameters and the documented responses are all on
 * {@link ProductsRestService}. What this class adds is {@code @RestController}, which is the
 * declaration that a returned object is the response body rather than the name of a view. The
 * contract says where an operation lives; the implementation says that it is one.
 */
@RestController
public class ProductsRestServiceImpl extends AbstractRestServiceImpl<ProductsService> implements ProductsRestService {

    public ProductsRestServiceImpl(ProductsService serviceWorker, CurrentRequesterProvider currentRequesterProvider) {
        super(serviceWorker, currentRequesterProvider);
    }

    @Override
    public ProductsDto getOwnProducts(ProductSearchParametersDto parameters) {
        final var page = getServiceWorker().getOwnProducts(
                PageArguments.index(parameters.getPage()),
                PageArguments.size(parameters.getSize()),
                ProductSortOptionTransformer.instance.createInput(parameters.getSort()),
                SortDirectionTransformer.instance.createInput(parameters.getDirection()),
                parameters.getQuery(),
                currentRequesterId()
        );
        return ProductsTransformer.instance.createOutput(page);
    }

    @Override
    public ProductDto changeStatus(UUID id, ProductStatusDto statusDto) {
        final var status = ProductStatusTransformer.instance.createInput(statusDto);
        final var product = getServiceWorker().changeStatus(id, status, currentRequesterId());
        return ProductTransformer.instance.createOutput(product);
    }

    @Override
    public ProductDto create(NewProductDto newProductDto) {
        final var newProduct = NewProductTransformer.instance.createOutput(newProductDto);
        final var product = getServiceWorker().create(newProduct, currentRequesterId());
        return ProductTransformer.instance.createOutput(product);
    }

    @Override
    public ProductDto update(UUID id, UpdateProductDto updateProductDto) {
        final var updateProduct = UpdateProductTransformer.instance.createOutput(updateProductDto);
        final var product = getServiceWorker().update(id, updateProduct, currentRequesterId());
        return ProductTransformer.instance.createOutput(product);
    }

    @Override
    public ProductDto loadById(UUID id) {
        final var product = getServiceWorker().loadById(id, currentRequesterId());
        return ProductTransformer.instance.createOutput(product);
    }

    @Override
    public void delete(UUID id) {
        getServiceWorker().delete(id, currentRequesterId());
    }
}
