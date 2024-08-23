package com.compass.ecommerce_spring.entity.pk;

import com.compass.ecommerce_spring.entity.ProductStock;
import com.compass.ecommerce_spring.entity.Sale;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Embeddable
public class SaleItemPk {
    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductStock product;
}
