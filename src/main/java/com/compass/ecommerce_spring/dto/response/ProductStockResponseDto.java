package com.compass.ecommerce_spring.dto.response;

import java.math.BigDecimal;

public record ProductStockResponseDto(
        Long id,
        String name,
        Integer quantity,
        BigDecimal unitPrice,
        String category,
        Boolean inactive
) {
}
