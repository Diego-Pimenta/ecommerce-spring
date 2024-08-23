package com.compass.ecommerce_spring.repository;

import com.compass.ecommerce_spring.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}
