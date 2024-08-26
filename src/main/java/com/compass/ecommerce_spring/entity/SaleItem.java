package com.compass.ecommerce_spring.entity;

import com.compass.ecommerce_spring.entity.pk.SaleItemPk;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@EqualsAndHashCode(of = "id")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tb_sale_item")
public class SaleItem {
    @EmbeddedId
    private SaleItemPk id = new SaleItemPk();
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal discount;

    public SaleItem(Sale sale, ProductStock product, Integer quantity, BigDecimal price, BigDecimal discount) {
        id.setSale(sale);
        id.setProduct(product);
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
    }
}
