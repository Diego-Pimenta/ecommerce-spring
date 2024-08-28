package com.compass.ecommerce_spring.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateUserStatusRequestDto(
        @NotNull(message = "Invalid active: Active must be true or false")
        Boolean active
) {
}
