package com.compass.ecommerce_spring.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateProductStockRequestDto(
        @NotBlank(message = "Invalid name: Name must not be blank")
        @Size(max = 80, message = "Invalid name: Name must not be longer than 50 characters")
        String name,
        @NotNull(message = "Invalid quantity: Quantity must not be null")
        @Min(value = 0, message = "Invalid quantity: Quantity must be a positive number")
        Integer quantity,
        @NotNull(message = "Invalid unit price: Unit price must not be null")
        @Min(value = 0, message = "Invalid unit price: Unit price must be a positive number")
        @JsonProperty(value = "unit_price")
        BigDecimal unitPrice,
        @NotBlank(message = "Invalid category: Category must not be blank")
        @Size(max = 30, message = "Invalid category: Category must not be longer than 50 characters")
        String category,
        @NotBlank(message = "Invalid inactive status: Inactive status must be true or false")
        @JsonProperty("inactive_status")
        Boolean inactive
) {
}
