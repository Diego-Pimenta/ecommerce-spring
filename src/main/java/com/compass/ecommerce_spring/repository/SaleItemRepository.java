package com.compass.ecommerce_spring.repository;

import com.compass.ecommerce_spring.entity.SaleItem;
import com.compass.ecommerce_spring.entity.pk.SaleItemPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleItemRepository extends JpaRepository<SaleItem, SaleItemPk> {
}
