package com.compass.ecommerce_spring.service.mapper;

import com.compass.ecommerce_spring.dto.request.CreateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.request.UpdateProductStockRequestDto;
import com.compass.ecommerce_spring.dto.response.ProductStockResponseDto;
import com.compass.ecommerce_spring.entity.ProductStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Mapper(componentModel = "spring", imports = {StringUtils.class, Objects.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductStockMapper {

    @Mapping(target = "active", constant = "true")
    ProductStock createProductStockToEntity(CreateProductStockRequestDto createProductStockRequestDto);

    @Mappings({
            @Mapping(target = "name", expression = "java(StringUtils.hasText(updateProductStockRequestDto.name()) ? updateProductStockRequestDto.name() : productStock.getName())"),
            @Mapping(target = "quantity", expression = "java(!Objects.isNull(updateProductStockRequestDto.quantity()) ? updateProductStockRequestDto.quantity() : productStock.getQuantity())"),
            @Mapping(target = "unitPrice", expression = "java(!Objects.isNull(updateProductStockRequestDto.unitPrice()) ? updateProductStockRequestDto.unitPrice() : productStock.getUnitPrice())"),
            @Mapping(target = "category", expression = "java(StringUtils.hasText(updateProductStockRequestDto.category()) ? updateProductStockRequestDto.category() : productStock.getCategory())"),
            @Mapping(target = "active", expression = "java(!Objects.isNull(updateProductStockRequestDto.active()) ? updateProductStockRequestDto.active() : productStock.getActive())"),
    })
    ProductStock updateProductStockToEntity(ProductStock productStock, UpdateProductStockRequestDto updateProductStockRequestDto);

    ProductStockResponseDto toDto(ProductStock productStock);
}
