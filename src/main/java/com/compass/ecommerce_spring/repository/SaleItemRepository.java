package com.compass.ecommerce_spring.repository;

import com.compass.ecommerce_spring.entity.SaleItem;
import com.compass.ecommerce_spring.entity.pk.SaleItemPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SaleItemRepository extends JpaRepository<SaleItem, SaleItemPk> {

    @Modifying
    @Query("DELETE FROM SaleItem si WHERE si.id.sale.id = :id")
    void deleteBySaleId(Long id);
}
