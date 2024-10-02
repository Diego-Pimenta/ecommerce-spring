package com.compass.ecommerce_spring.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateProductStockRequestDto(
        @Size(max = 80, message = "Invalid name: Name must not be longer than 50 characters")
        String name,
        @Min(value = 0, message = "Invalid quantity: Quantity must be a positive number")
        Integer quantity,
        @Min(value = 0, message = "Invalid unit price: Unit price must be a positive number")
        @Max(value = 999999, message = "Invalid unit price: Unit price must not be greater than 999.999")
        @JsonProperty(value = "unit_price")
        BigDecimal unitPrice,
        @Size(max = 30, message = "Invalid category: Category must not be longer than 50 characters")
        String category,
        Boolean active
) {
}
