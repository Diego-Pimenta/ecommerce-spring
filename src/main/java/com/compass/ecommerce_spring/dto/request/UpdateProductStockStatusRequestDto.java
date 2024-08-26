package com.compass.ecommerce_spring.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record UpdateProductStockStatusRequestDto(
        @NotNull(message = "Invalid inactive status: Inactive status must be true or false")
        @JsonProperty("inactive_status")
        Boolean inactive
) {
}
