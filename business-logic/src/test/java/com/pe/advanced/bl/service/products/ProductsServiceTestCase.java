package com.pe.advanced.bl.service.products;

import com.pe.advanced.bl.service.AbstractCrudServiceTestCase;
import com.pe.advanced.bl.service.ProductsService;
import com.pe.advanced.bl.service.impl.product.ProductsServiceImpl;
import com.pe.advanced.dao.api.product.ProductDao;
import com.pe.advanced.domain.exceptions.AuthorizationException;
import com.pe.advanced.domain.exceptions.NotFoundException;
import com.pe.advanced.domain.product.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductsServiceTestCase extends AbstractCrudServiceTestCase<Product, NewProduct, UpdateProduct, ProductDao, ProductsService> {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductsServiceImpl productsService;

    @Test
    void create_ShouldMapAllFieldsThroughNewProductTransformer() {
        final NewProduct newDto = createValidNewDomain();
        when(productDao.create(any())).thenAnswer(invocation -> withId(invocation.getArgument(0), UUID.randomUUID()));

        final Product result = productsService.create(newDto, requesterId);
        assertEquals(newDto.getName(), result.getName());
        assertEquals(newDto.getSku(), result.getSku());
        assertEquals(0, newDto.getPrice().compareTo(result.getPrice()));
        assertEquals(ProductStatus.DRAFT, result.getStatus());
    }

    @Test
    void update_ShouldMapAllFieldsThroughUpdateProductTransformer() {
        final Product existing = persistedProduct(requesterId, ProductStatus.DRAFT);
        final UpdateProduct updateDto = createUpdateDomain();

        when(productDao.loadById(existing.getId())).thenReturn(existing);
        when(productDao.update(existing)).thenReturn(existing);

        final Product result = productsService.update(existing.getId(), updateDto, requesterId);
        assertEquals(updateDto.getName(), result.getName());
        assertEquals(updateDto.getSku(), result.getSku());
        assertEquals(0, updateDto.getPrice().compareTo(result.getPrice()));
    }

    @Test
    void changeStatus_WhenRequesterIdIsNull_ShouldThrowAuthorizationException() {
        assertThrows(AuthorizationException.class, () -> productsService.changeStatus(UUID.randomUUID(), ProductStatus.ACTIVE, null));
        verifyNoInteractions(productDao);
    }

    @Test
    void changeStatus_WhenEntityDoesNotExist_ShouldThrowNotFoundException() {
        final UUID missingId = UUID.randomUUID();
        when(productDao.loadById(missingId)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> productsService.changeStatus(missingId, ProductStatus.ACTIVE, requesterId));
        verify(productDao, never()).update(any());
    }

    @Test
    void changeStatus_WhenRequesterIsNotOwner_ShouldThrowAuthorizationExceptionAndNeverPersist() {
        final Product existing = persistedProduct(alternativeRequesterId, ProductStatus.DRAFT);
        when(productDao.loadById(existing.getId())).thenReturn(existing);
        assertThrows(AuthorizationException.class, () -> productsService.changeStatus(existing.getId(), ProductStatus.ACTIVE, requesterId));
        verify(productDao, never()).update(any());
    }

    @Test
    void changeStatus_WhenTransitionIsValid_ShouldPersistNewStatus() {
        final Product existing = persistedProduct(requesterId, ProductStatus.DRAFT);
        when(productDao.loadById(existing.getId())).thenReturn(existing);
        when(productDao.update(existing)).thenReturn(existing);

        productsService.changeStatus(existing.getId(), ProductStatus.ACTIVE, requesterId);
        assertEquals(ProductStatus.ACTIVE, existing.getStatus());
        verify(productDao, times(1)).update(existing);
    }

    @Test
    void changeStatus_WhenTransitionIsIllegal_ShouldThrowAndNeverPersist() {
        final Product existing = persistedProduct(requesterId, ProductStatus.ARCHIVED);
        when(productDao.loadById(existing.getId())).thenReturn(existing);
        assertThrows(IllegalStateException.class, () -> productsService.changeStatus(existing.getId(), ProductStatus.ACTIVE, requesterId));
        verify(productDao, never()).update(any());
    }

    @Test
    void create_ShouldBuildSpecificationFromDimensionsAndWeight() {
        final NewProduct newDto = createValidNewDomain();
        when(productDao.create(any())).thenAnswer(invocation -> withId(invocation.getArgument(0), UUID.randomUUID()));

        final Product result = productsService.create(newDto, requesterId);

        assertNotNull(result.getSpecification());
        assertEquals(newDto.getDimensions(), result.getSpecification().getDimensions());
        assertEquals(newDto.getWeight(), result.getSpecification().getWeight());
    }

    @Test
    void update_WhenSpecificationAlreadyExists_ShouldMutateInPlaceRatherThanReplace() {
        final Product existing = persistedProduct(requesterId, ProductStatus.DRAFT);
        existing.setSpecification(new ProductSpecification("10x10x10cm", 450));
        final ProductSpecification originalSpecification = existing.getSpecification();

        final UpdateProduct updateDto = createUpdateDomain();

        when(productDao.loadById(existing.getId())).thenReturn(existing);
        when(productDao.update(existing)).thenReturn(existing);

        final Product result = productsService.update(existing.getId(), updateDto, requesterId);

        // The transformer's else-branch mutates the existing specification rather than
        // allocating a new one - this asserts that behavior directly, not just its effect.
        assertSame(originalSpecification, result.getSpecification());
        assertEquals(updateDto.getDimensions(), result.getSpecification().getDimensions());
        assertEquals(updateDto.getWeight(), result.getSpecification().getWeight());
    }

    @Override
    protected ProductsService getService() {
        return productsService;
    }

    @Override
    protected ProductDao getMockDao() {
        return productDao;
    }

    @Override
    protected NewProduct createValidNewDomain() {
        final var dto = new NewProduct();
        dto.setName("LEGO Star Wars");
        dto.setSku("LEGO-75355");
        dto.setPrice(BigDecimal.valueOf(239.99));
        dto.setDimensions("42x35x8cm");
        dto.setWeight(2500);
        return dto;
    }

    @Override
    protected UpdateProduct createUpdateDomain() {
        final var dto = new UpdateProduct();
        dto.setName("LEGO Star Wars (Retired Edition)");
        dto.setSku("LEGO-75355-R");
        dto.setPrice(BigDecimal.valueOf(199.99));
        dto.setDimensions("40x33x7cm");
        dto.setWeight(2300);
        return dto;
    }

    @Override
    protected Product withId(Product transientProduct, UUID id) {
        return new Product(
                id,
                transientProduct.getCreatedById(),
                transientProduct.getUpdatedById(),
                transientProduct.getName(),
                transientProduct.getSku(),
                transientProduct.getPrice(),
                transientProduct.getSpecification()
        );
    }

    private Product persistedProduct(UUID ownerId, ProductStatus status) {
        final var product = new Product(UUID.randomUUID(), Instant.now(), Instant.now(), status);
        product.setName("Existing Product");
        product.setSku("EXISTING-SKU");
        product.setPrice(BigDecimal.TEN);
        product.setCreatedById(ownerId);
        product.setUpdatedById(ownerId);
        return product;
    }
}