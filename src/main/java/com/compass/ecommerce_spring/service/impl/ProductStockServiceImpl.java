package com.compass.ecommerce_spring.service.impl;

import com.compass.ecommerce_spring.dto.request.ProductStockRequestDto;
import com.compass.ecommerce_spring.dto.response.ProductStockResponseDto;
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

@RequiredArgsConstructor
@Transactional
@Service
public class ProductStockServiceImpl implements ProductStockService {

    private final ProductStockRepository repository;
    private final ProductStockMapper mapper;

    @CacheEvict(value = "products", allEntries = true)
    @Override
    public ProductStockResponseDto save(ProductStockRequestDto productStockRequestDto) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public ProductStockResponseDto findById(Long id) {
        return null;
    }

    @Cacheable(value = "products")
    @Transactional(readOnly = true)
    @Override
    public List<ProductStockResponseDto> findAll() {
        return List.of();
    }

    @CachePut(value = "products", key = "#id")
    @Override
    public ProductStockResponseDto update(Long id, ProductStockRequestDto productStockRequestDto) {
        return null;
    }

    @CacheEvict(value = "products", key = "#id")
    @Override
    public void delete(Long id) {
    }
}
