package com.compass.ecommerce_spring.dto.request;

import jakarta.validation.constraints.Email;

public record PasswordResetRequestDto(
        @Email(message = "Invalid email")
        String email
) {
}
