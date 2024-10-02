package com.compass.ecommerce_spring.dto.response;

import com.compass.ecommerce_spring.entity.enums.SaleStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonPropertyOrder({"id", "moment", "status", "customer", "items", "total"})
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public record SaleResponseDto(
        Long id,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        LocalDateTime moment,
        SaleStatus status,
        UserResponseDto customer,
        List<SaleItemResponseDto> items,
        BigDecimal total
) {
}
