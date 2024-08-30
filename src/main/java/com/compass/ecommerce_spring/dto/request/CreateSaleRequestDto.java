package com.compass.ecommerce_spring.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CreateSaleRequestDto(
        @NotBlank(message = "Invalid customer cpf: customer cpf must not be blank")
        @Size(min = 11, max = 11, message = "Invalid customer cpf")
        @JsonProperty("customer_cpf")
        String customerCpf,
        @NotEmpty(message = "Invalid items: Items must not be empty")
        Set<@Valid SaleItemRequestDto> items
) {
}
