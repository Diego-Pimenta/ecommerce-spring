package com.compass.ecommerce_spring.repository;

import com.compass.ecommerce_spring.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
}
