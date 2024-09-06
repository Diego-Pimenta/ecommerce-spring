package com.compass.ecommerce_spring.service;

import com.compass.ecommerce_spring.dto.request.SaleRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateSaleStatusRequestDto;
import com.compass.ecommerce_spring.dto.response.SaleResponseDto;

import java.util.List;

public interface SaleService {

    SaleResponseDto save(SaleRequestDto saleRequestDto);

    SaleResponseDto findById(Long id);

    List<SaleResponseDto> findAll(Integer page, Integer size, String orderBy, String direction);

    SaleResponseDto update(Long id, SaleRequestDto saleRequestDto);

    SaleResponseDto updateStatus(Long id, UpdateSaleStatusRequestDto updateSaleStatusRequestDto);

    void delete(Long id);
}
