package com.compass.ecommerce_spring.repository;

import com.compass.ecommerce_spring.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT s FROM Sale s JOIN FETCH s.items")
    List<Sale> findAllFetchItems();

    @Query("SELECT s FROM Sale s JOIN FETCH s.items WHERE s.id = :id")
    Optional<Sale> findByIdFetchItems(Long id);
}
