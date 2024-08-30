package com.compass.ecommerce_spring.service.mapper;

import com.compass.ecommerce_spring.entity.Sale;
import com.compass.ecommerce_spring.entity.SaleItem;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface MonetaryOperations {

    default BigDecimal getDiscountPercentage(Integer quantity) {
        BigDecimal discount;
        if (quantity == 1) {
            discount = new BigDecimal("0.00");
        } else if (quantity < 4) {
            discount = new BigDecimal("0.05");
        } else {
            discount = new BigDecimal("0.10");
        }
        return discount;
    }

    default BigDecimal calculateSubTotal(SaleItem saleItem) {
        return saleItem.getPrice()
                .subtract(discountValue(saleItem))
                .multiply(BigDecimal.valueOf(saleItem.getQuantity()))
                .setScale(2, RoundingMode.HALF_UP);
    }

    default BigDecimal calculateTotal(Sale sale) {
        return sale.getItems()
                .stream()
                .map(this::calculateSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    default BigDecimal discountValue(SaleItem saleItem) {
        return saleItem.getPrice().multiply(saleItem.getDiscount());
    }
}
