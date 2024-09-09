package com.compass.ecommerce_spring.dto.response;

import com.compass.ecommerce_spring.entity.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"cpf", "name", "email", "phone_number", "address", "role", "active"})
public record UserResponseDto(
        String cpf,
        String name,
        String email,
        @JsonProperty("phone_number")
        String phoneNumber,
        String address,
        Role role,
        // não mostra a propriedade no json
//        @JsonIgnore
        Boolean active
) {
}
