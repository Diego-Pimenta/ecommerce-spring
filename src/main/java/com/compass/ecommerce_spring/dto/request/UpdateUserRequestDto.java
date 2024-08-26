package com.compass.ecommerce_spring.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequestDto(
        @Email(message = "Invalid email")
        String email,
        @NotBlank(message = "Invalid password: Password must not be blank")
        @Size(min = 8, max = 20, message = "Invalid password: Password must be between 8 and 20 characters")
        String password,
        @NotBlank(message = "Invalid phone number: Phone number must not be blank")
        @Pattern(regexp = "([(]?\\d{2}[)]?)\\d{5}-?\\d{4}", message = "Invalid phone number")
        String phoneNumber,
        @NotBlank(message = "Invalid address: Address must not be blank")
        @Size(max = 100, message = "Invalid address: Address must not be longer than 100 characters")
        String address
) {
}
