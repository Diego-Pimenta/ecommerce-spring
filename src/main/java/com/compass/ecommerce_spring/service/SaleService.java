package com.compass.ecommerce_spring.service;

import com.compass.ecommerce_spring.dto.request.CreateSaleRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateSaleRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateSaleStatusRequestDto;
import com.compass.ecommerce_spring.dto.response.SaleResponseDto;

import java.util.List;

public interface SaleService {

    SaleResponseDto save(CreateSaleRequestDto createSaleRequestDto);

    SaleResponseDto findById(Long id);

    List<SaleResponseDto> findAll();

    SaleResponseDto update(Long id, UpdateSaleRequestDto updateSaleRequestDto);

    SaleResponseDto updateStatus(Long id, UpdateSaleStatusRequestDto updateSaleStatusRequestDto);

    void delete(Long id);
}
