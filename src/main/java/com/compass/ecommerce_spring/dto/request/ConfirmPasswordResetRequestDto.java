package com.compass.ecommerce_spring.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConfirmPasswordResetRequestDto(
        @NotBlank(message = "Invalid new password: New password must not be blank")
        @Size(min = 8, max = 20, message = "Invalid new password: New password must be between 8 and 20 characters")
        @JsonProperty("new_password")
        String newPassword
) {
}
