package com.compass.ecommerce_spring.service;

import com.compass.ecommerce_spring.exception.custom.ResourceAlreadyExistsException;
import com.compass.ecommerce_spring.exception.custom.ResourceNotFoundException;
import com.compass.ecommerce_spring.repository.ProductStockRepository;
import com.compass.ecommerce_spring.service.impl.ProductStockServiceImpl;
import com.compass.ecommerce_spring.service.mapper.ProductStockMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static com.compass.ecommerce_spring.common.ProductConstants.CREATE_PRODUCT_REQUEST;
import static com.compass.ecommerce_spring.common.ProductConstants.PRODUCT;
import static com.compass.ecommerce_spring.common.ProductConstants.PRODUCTS;
import static com.compass.ecommerce_spring.common.ProductConstants.PRODUCTS_RESPONSE;
import static com.compass.ecommerce_spring.common.ProductConstants.PRODUCT_RESPONSE;
import static com.compass.ecommerce_spring.common.ProductConstants.UPDATE_ACTIVE_STATUS_REQUEST;
import static com.compass.ecommerce_spring.common.ProductConstants.UPDATE_PRODUCT_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductStockRepository repository;
    @Mock
    private ProductStockMapper mapper;

    @InjectMocks
    private ProductStockServiceImpl service;

    @Test
    public void saveProduct_WithValidData_ReturnsProductResponseDto() {
        when(repository.existsByName(CREATE_PRODUCT_REQUEST.name())).thenReturn(false);
        when(mapper.createProductStockToEntity(CREATE_PRODUCT_REQUEST)).thenReturn(PRODUCT);
        when(repository.save(any())).thenReturn(PRODUCT);
        when(mapper.toDto(any())).thenReturn(PRODUCT_RESPONSE);

        var sut = service.save(CREATE_PRODUCT_REQUEST);

        assertThat(sut).isEqualTo(PRODUCT_RESPONSE);
    }

    @Test
    public void saveProduct_WithExistingName_ThrowsException() {
        when(repository.existsByName(CREATE_PRODUCT_REQUEST.name())).thenReturn(true);

        assertThatThrownBy(() -> service.save(CREATE_PRODUCT_REQUEST)).isInstanceOf(ResourceAlreadyExistsException.class);
    }

    @Test
    public void getProduct_ByExistingId_ReturnsProductResponseDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(PRODUCT));
        when(mapper.toDto(any())).thenReturn(PRODUCT_RESPONSE);

        var sut = service.findById(1L);

        assertThat(sut).isNotNull();
        assertThat(sut).isEqualTo(PRODUCT_RESPONSE);
    }

    @Test
    public void getProduct_ByNonexistentId_ThrowsException() {
        when(repository.findById(0L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(0L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void getAllProducts_ReturnsListOfProductResponseDto() {
        var page = 0;
        var size = 10;
        var orderBy = "name";
        var direction = "ASC";

        var pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(direction), orderBy);
        var productPage = new PageImpl<>(PRODUCTS, pageRequest, PRODUCTS.size());

        when(repository.findAll(pageRequest)).thenReturn(productPage);
        when(mapper.toDto(any())).thenReturn(PRODUCT_RESPONSE);

        var sut = service.findAll(page, size, orderBy, direction);

        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(1);
        assertThat(sut.get(0)).isEqualTo(PRODUCTS_RESPONSE.get(0));
    }

    @Test
    public void updateProduct_WithExistingId_ReturnsProductResponseDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(PRODUCT));
        when(repository.findByName(UPDATE_PRODUCT_REQUEST.name())).thenReturn(Optional.empty());
        when(mapper.updateProductStockToEntity(any(), any())).thenReturn(PRODUCT);
        when(repository.save(any())).thenReturn(PRODUCT);
        when(mapper.toDto(any())).thenReturn(PRODUCT_RESPONSE);

        var sut = service.update(1L, UPDATE_PRODUCT_REQUEST);

        assertThat(sut).isEqualTo(PRODUCT_RESPONSE);
    }

    @Test
    public void updateProduct_WithNonexistentId_ThrowsException() {
        when(repository.findById(0L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(0L, UPDATE_PRODUCT_REQUEST)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void updateProduct_WithExistingName_ThrowsException() {
        when(repository.findById(1L)).thenReturn(Optional.of(PRODUCT));
        when(repository.findByName(UPDATE_PRODUCT_REQUEST.name())).thenReturn(Optional.of(PRODUCT));

        assertThatThrownBy(() -> service.update(1L, UPDATE_PRODUCT_REQUEST)).isInstanceOf(ResourceAlreadyExistsException.class);
    }

    @Test
    public void updateProductStatus_WithExistingId_ReturnsProductResponseDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(PRODUCT));
        when(repository.save(any())).thenReturn(PRODUCT);
        when(mapper.toDto(any())).thenReturn(PRODUCT_RESPONSE);

        var sut = service.updateStatus(1L, UPDATE_ACTIVE_STATUS_REQUEST);

        assertThat(sut).isEqualTo(PRODUCT_RESPONSE);
    }

    @Test
    public void updateProductStatus_WithNonexistentId_ThrowsException() {
        when(repository.findById(0L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateStatus(0L, UPDATE_ACTIVE_STATUS_REQUEST)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void deleteProduct_WithExistingId_DoesNotThrowAnyException() {
        when(repository.findById(1L)).thenReturn(Optional.of(PRODUCT));

        service.delete(1L);
    }

    @Test
    public void deleteProduct_WithNonexistentId_ThrowsException() {
        when(repository.findById(0L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(0L)).isInstanceOf(ResourceNotFoundException.class);
    }
}
