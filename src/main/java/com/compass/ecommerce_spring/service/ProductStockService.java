package com.compass.ecommerce_spring.service;

import com.compass.ecommerce_spring.dto.request.ProductStockRequestDto;
import com.compass.ecommerce_spring.dto.response.ProductStockResponseDto;

import java.util.List;

public interface ProductStockService {

    ProductStockResponseDto save(ProductStockRequestDto productStockRequestDto);
    ProductStockResponseDto findById(Long id);
    List<ProductStockResponseDto> findAll();
    ProductStockResponseDto update(Long id, ProductStockRequestDto productStockRequestDto);
    void delete(Long id);
}
