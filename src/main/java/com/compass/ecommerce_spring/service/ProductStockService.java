package com.compass.ecommerce_spring.service;

import com.compass.ecommerce_spring.dto.request.CreateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateActiveStatusRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.response.ProductStockResponseDto;

import java.util.List;

public interface ProductStockService {

    ProductStockResponseDto save(CreateProductStockRequestDto createProductStockRequestDto);

    ProductStockResponseDto findById(Long id);

    List<ProductStockResponseDto> findAll();

    ProductStockResponseDto update(Long id, UpdateProductStockRequestDto updateProductStockRequestDto);

    ProductStockResponseDto updateStatus(Long id, UpdateActiveStatusRequestDto updateActiveStatusRequestDto);

    void delete(Long id);
}
