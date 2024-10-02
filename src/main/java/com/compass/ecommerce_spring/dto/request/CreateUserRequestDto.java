package com.compass.ecommerce_spring.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequestDto(
        @NotBlank(message = "Invalid cpf: cpf must not be blank")
        @Size(min = 11, max = 11, message = "Invalid cpf")
        String cpf,
        @NotBlank(message = "Invalid name: Name must not be blank")
        @Size(max = 80, message = "Invalid name: Name must not be longer than 80 characters")
        String name,
        @NotBlank(message = "Invalid email: Email must not be blank")
        @Email(message = "Invalid email: Email pattern does not match")
        String email,
        @NotBlank(message = "Invalid password: Password must not be blank")
        @Size(min = 8, max = 20, message = "Invalid password: Password must be between 8 and 20 characters")
        String password,
        @NotBlank(message = "Invalid phone number: Phone number must not be blank")
        @Pattern(regexp = "([(]?\\d{2}[)]?)\\d{5}-?\\d{4}", message = "Invalid phone number")
        @JsonProperty("phone_number")
        String phoneNumber,
        @NotBlank(message = "Invalid address: Address must not be blank")
        @Size(max = 100, message = "Invalid address: Address must not be longer than 100 characters")
        String address
) {
}
