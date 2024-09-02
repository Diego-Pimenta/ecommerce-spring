package com.compass.ecommerce_spring.service.impl;

import com.compass.ecommerce_spring.dto.request.CreateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateActiveStatusRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.response.ProductStockResponseDto;
import com.compass.ecommerce_spring.exception.custom.ResourceAlreadyExistsException;
import com.compass.ecommerce_spring.exception.custom.ResourceNotFoundException;
import com.compass.ecommerce_spring.repository.ProductStockRepository;
import com.compass.ecommerce_spring.service.ProductStockService;
import com.compass.ecommerce_spring.service.mapper.ProductStockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductStockServiceImpl implements ProductStockService {

    private final ProductStockRepository repository;
    private final ProductStockMapper mapper;

    @CacheEvict(value = "products", allEntries = true)
    @Override
    public ProductStockResponseDto save(CreateProductStockRequestDto createProductStockRequestDto) {
        if (repository.existsByName(createProductStockRequestDto.name())) {
            throw new ResourceAlreadyExistsException("A product with this name already exists");
        }

        var productStock = mapper.createProductStockToEntity(createProductStockRequestDto);
        var createdProductStock = repository.save(productStock);
        return mapper.toDto(createdProductStock);
    }

    @Cacheable(value = "products", key = "#id")
    @Transactional(readOnly = true)
    @Override
    public ProductStockResponseDto findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in stock"));
    }

    @Cacheable(value = "products")
    @Transactional(readOnly = true)
    @Override
    public List<ProductStockResponseDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @CachePut(value = "products", key = "#id")
    @Override
    public ProductStockResponseDto update(Long id, UpdateProductStockRequestDto updateProductStockRequestDto) {
        var existingProduct = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in stock"));

        var existingProductByName = repository.findByName(updateProductStockRequestDto.name());

        if (existingProductByName.isPresent() && !existingProductByName.get().getId().equals(id)) {
            throw new ResourceAlreadyExistsException("A product with this name already exists");
        }

        var productStock = mapper.updateProductStockToEntity(existingProduct, updateProductStockRequestDto);
        var updatedProductStock = repository.save(productStock);
        return mapper.toDto(updatedProductStock);
    }

    @CachePut(value = "products", key = "#id")
    @Override
    public ProductStockResponseDto updateStatus(Long id, UpdateActiveStatusRequestDto updateActiveStatusRequestDto) {
        var product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in stock"));

        product.setActive(updateActiveStatusRequestDto.active());
        var updatedProduct = repository.save(product);
        return mapper.toDto(updatedProduct);
    }

    @CacheEvict(value = "products", allEntries = true)
    @Override
    public void delete(Long id) {
        var product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in stock"));

        if (product.getItems().isEmpty()) {
            repository.deleteById(id);
        } else {
            product.setActive(false);
            repository.save(product);
        }
    }
}
