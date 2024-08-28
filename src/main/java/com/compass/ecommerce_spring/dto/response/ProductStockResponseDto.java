package com.compass.ecommerce_spring.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

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
