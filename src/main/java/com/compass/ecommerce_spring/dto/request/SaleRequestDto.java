package com.compass.ecommerce_spring.dto.request;

import com.compass.ecommerce_spring.validation.UniqueProductId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SaleRequestDto(
        @UniqueProductId
        @NotEmpty(message = "Invalid items: Items must not be empty")
        List<@Valid SaleItemRequestDto> items
) {
}
