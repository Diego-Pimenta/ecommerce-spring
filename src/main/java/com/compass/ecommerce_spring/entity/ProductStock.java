package com.compass.ecommerce_spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(of = "id")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_product_stock")
public class ProductStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 80)
    private String name;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false, name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;
    @Column(nullable = false, length = 50)
    private String category;
    @Column(nullable = false)
    private Boolean inactive = false; // para ser inativado ele deve ser "deletado" primeiro
    @OneToMany(mappedBy = "id.product")
    private Set<SaleItem> items = new HashSet<>();
}
