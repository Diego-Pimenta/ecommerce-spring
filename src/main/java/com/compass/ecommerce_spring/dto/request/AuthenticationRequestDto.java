package com.compass.ecommerce_spring.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationRequestDto(
        @NotBlank(message = "Invalid cpf: Cpf must not be blank")
        @Size(min = 11, max = 11, message = "Invalid cpf")
        String cpf,
        @NotBlank(message = "Invalid password: Password must not be blank")
        @Size(min = 8, max = 20, message = "Invalid password: Password must be between 8 and 20 characters")
        String password
) {
}
