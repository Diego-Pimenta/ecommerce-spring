package com.compass.ecommerce_spring.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public record ProductStockResponseDto(
        Long id,
        String name,
        Integer quantity,
        @JsonProperty("unit_price")
        BigDecimal unitPrice,
        String category,
        Boolean active
) {
}
