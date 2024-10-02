package com.compass.ecommerce_spring.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequestDto(
        @Email(message = "Invalid email: Email pattern does not match")
        String email,
        @Size(min = 8, max = 20, message = "Invalid password: Password must be between 8 and 20 characters")
        String password,
        @Pattern(regexp = "([(]?\\d{2}[)]?)\\d{5}-?\\d{4}", message = "Invalid phone number")
        @JsonProperty("phone_number")
        String phoneNumber,
        @Size(max = 100, message = "Invalid address: Address must not be longer than 100 characters")
        String address
) {
}
