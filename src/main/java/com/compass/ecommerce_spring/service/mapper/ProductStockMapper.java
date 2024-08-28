package com.compass.ecommerce_spring.service.mapper;

import com.compass.ecommerce_spring.dto.request.CreateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateProductStockStatusRequestDto;
import com.compass.ecommerce_spring.dto.response.ProductStockResponseDto;
import com.compass.ecommerce_spring.entity.ProductStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductStockMapper {

    ProductStock createProductStockToEntity(CreateProductStockRequestDto createProductStockRequestDto);

    @Mappings({
            @Mapping(target = "name", expression = "java(updateProductStockRequestDto.name())"),
            @Mapping(target = "quantity", expression = "java(updateProductStockRequestDto.quantity())"),
            @Mapping(target = "unitPrice", expression = "java(updateProductStockRequestDto.unitPrice())"),
            @Mapping(target = "category", expression = "java(updateProductStockRequestDto.category())"),
            @Mapping(target = "active", expression = "java(updateProductStockRequestDto.active())"),
    })
    ProductStock updateProductStockToEntity(ProductStock productStock, UpdateProductStockRequestDto updateProductStockRequestDto);

    @Mapping(target = "active", expression = "java(updateProductStockStatusRequestDto.active())")
    ProductStock updateProductStockStatusToEntity(ProductStock productStock, UpdateProductStockStatusRequestDto updateProductStockStatusRequestDto);

    ProductStockResponseDto toDto(ProductStock productStock);
}
