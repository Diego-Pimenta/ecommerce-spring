package com.compass.ecommerce_spring.service;

import com.compass.ecommerce_spring.dto.request.CreateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateActiveStatusRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateProductStockRequestDto;
import com.compass.ecommerce_spring.exception.custom.ResourceAlreadyExistsException;
import com.compass.ecommerce_spring.exception.custom.ResourceNotFoundException;
import com.compass.ecommerce_spring.repository.ProductStockRepository;
import com.compass.ecommerce_spring.service.impl.ProductStockServiceImpl;
import com.compass.ecommerce_spring.service.mapper.ProductStockMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.compass.ecommerce_spring.common.ProductConstants.PRODUCT;
import static com.compass.ecommerce_spring.common.ProductConstants.PRODUCTS;
import static com.compass.ecommerce_spring.common.ProductConstants.PRODUCTS_RESPONSE_DTO;
import static com.compass.ecommerce_spring.common.ProductConstants.PRODUCT_RESPONSE_DTO;
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

    private CreateProductStockRequestDto createProductStockRequestDto;
    private UpdateProductStockRequestDto updateProductStockRequestDto;
    private UpdateActiveStatusRequestDto updateActiveStatusRequestDto;

    @BeforeEach
    public void setup() {
        createProductStockRequestDto = new CreateProductStockRequestDto("Cigarettes", 13, new BigDecimal("3.99"), "Drugs");
        updateProductStockRequestDto = new UpdateProductStockRequestDto("Cigarettes", 7, new BigDecimal("4.99"), "Drugs", true);
        updateActiveStatusRequestDto = new UpdateActiveStatusRequestDto(true);
    }

    @Test
    public void saveProduct_WithValidData_ReturnsProductResponseDto() {
        when(repository.existsByName(createProductStockRequestDto.name())).thenReturn(false);
        when(mapper.createProductStockToEntity(createProductStockRequestDto)).thenReturn(PRODUCT);
        when(repository.save(any())).thenReturn(PRODUCT);
        when(mapper.toDto(any())).thenReturn(PRODUCT_RESPONSE_DTO);

        var sut = service.save(createProductStockRequestDto);

        assertThat(sut).isEqualTo(PRODUCT_RESPONSE_DTO);
    }

    @Test
    public void saveProduct_WithExistingName_ThrowsException() {
        when(repository.existsByName(createProductStockRequestDto.name())).thenReturn(true);

        assertThatThrownBy(() -> service.save(createProductStockRequestDto)).isInstanceOf(ResourceAlreadyExistsException.class);
    }

    @Test
    public void getProduct_ByExistingId_ReturnsProductResponseDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(PRODUCT));
        when(mapper.toDto(any())).thenReturn(PRODUCT_RESPONSE_DTO);

        var sut = service.findById(1L);

        assertThat(sut).isNotNull();
        assertThat(sut).isEqualTo(PRODUCT_RESPONSE_DTO);
    }

    @Test
    public void getProduct_ByNonexistentId_ThrowsException() {
        when(repository.findById(0L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(0L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void getAllProducts_ReturnsListOfProductResponseDto() {
        when(repository.findAll()).thenReturn(PRODUCTS);
        when(mapper.toDto(any())).thenReturn(PRODUCT_RESPONSE_DTO);

        var sut = service.findAll();

        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(1);
        assertThat(sut.get(0)).isEqualTo(PRODUCTS_RESPONSE_DTO.get(0));
    }

    @Test
    public void updateProduct_WithExistingId_ReturnsProductResponseDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(PRODUCT));
        when(repository.findByName(updateProductStockRequestDto.name())).thenReturn(Optional.empty());
        when(mapper.updateProductStockToEntity(any(), any())).thenReturn(PRODUCT);
        when(repository.save(any())).thenReturn(PRODUCT);
        when(mapper.toDto(any())).thenReturn(PRODUCT_RESPONSE_DTO);

        var sut = service.update(1L, updateProductStockRequestDto);

        assertThat(sut).isEqualTo(PRODUCT_RESPONSE_DTO);
    }

    @Test
    public void updateProduct_WithNonexistentId_ThrowsException() {
        when(repository.findById(0L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(0L, updateProductStockRequestDto)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void updateProduct_WithExistingName_ThrowsException() {
        when(repository.findById(1L)).thenReturn(Optional.of(PRODUCT));
        when(repository.findByName(updateProductStockRequestDto.name())).thenReturn(Optional.of(PRODUCT));

        assertThatThrownBy(() -> service.update(1L, updateProductStockRequestDto)).isInstanceOf(ResourceAlreadyExistsException.class);
    }

    @Test
    public void updateProductStatus_WithExistingId_ReturnsProductResponseDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(PRODUCT));
        when(repository.save(any())).thenReturn(PRODUCT);
        when(mapper.toDto(any())).thenReturn(PRODUCT_RESPONSE_DTO);

        var sut = service.updateStatus(1L, updateActiveStatusRequestDto);

        assertThat(sut).isEqualTo(PRODUCT_RESPONSE_DTO);
    }

    @Test
    public void updateProductStatus_WithNonexistentId_ThrowsException() {
        when(repository.findById(0L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateStatus(0L, updateActiveStatusRequestDto)).isInstanceOf(ResourceNotFoundException.class);
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
