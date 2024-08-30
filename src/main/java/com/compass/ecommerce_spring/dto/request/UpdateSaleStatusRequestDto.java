package com.compass.ecommerce_spring.dto.request;

import com.compass.ecommerce_spring.entity.enums.SaleStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateSaleStatusRequestDto(
        @NotNull(message = "Invalid status: Status must be WAITING_PAYMENT, PAID, SHIPPED, DONE or CANCELLED")
        SaleStatus status
) {
}
