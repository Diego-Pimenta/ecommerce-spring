package com.compass.ecommerce_spring.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;

@JsonPropertyOrder({"product", "quantity", "price", "discount", "sub_total"})
public record SaleItemResponseDto(
        ProductStockResponseDto product,
        Integer quantity,
        BigDecimal price,
        BigDecimal discount,
        @JsonProperty("sub_total")
        BigDecimal subTotal
) {
}
