package com.compass.ecommerce_spring.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record SaleItemResponseDto(
        ProductStockResponseDto product,
        Integer quantity,
        BigDecimal price,
        BigDecimal discount,
        @JsonProperty("sub_total")
        BigDecimal subTotal
) {
}
