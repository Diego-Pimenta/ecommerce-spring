package com.compass.ecommerce_spring.repository;

import com.compass.ecommerce_spring.entity.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {

    Optional<ProductStock> findByName(String name);
}
