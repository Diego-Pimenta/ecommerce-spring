package com.compass.ecommerce_spring.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SaleItemRequestDto(
        @NotNull(message = "Invalid product id: Product id must not be null")
        @Min(value = 0, message = "Invalid product id: Product id must be a positive number")
        @JsonProperty("product_id")
        Long productId,
        @NotNull(message = "Invalid quantity: Quantity must not be null")
        @Min(value = 0, message = "Invalid quantity: Quantity must be a positive number")
        Integer quantity
) {
}
