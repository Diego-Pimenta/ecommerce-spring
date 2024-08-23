package com.compass.ecommerce_spring.repository;

import com.compass.ecommerce_spring.entity.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
}
