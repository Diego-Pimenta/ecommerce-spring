package com.compass.ecommerce_spring.service.mapper;

import com.compass.ecommerce_spring.dto.request.CreateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateProductStockStatusRequestDto;
import com.compass.ecommerce_spring.dto.response.ProductStockResponseDto;
import com.compass.ecommerce_spring.entity.ProductStock;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductStockMapper {

    ProductStock createProductStockToEntity(CreateProductStockRequestDto createProductStockRequestDto);

    ProductStock updateProductStockToEntity(Long id, UpdateProductStockRequestDto updateProductStockRequestDto);

    ProductStock updateProductStockStatusToEntity(Long id, UpdateProductStockStatusRequestDto updateProductStockStatusRequestDto);

    ProductStockResponseDto toDto(ProductStock productStock);
}
