package com.compass.ecommerce_spring.repository;

import com.compass.ecommerce_spring.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT s FROM Sale s JOIN FETCH s.items")
    List<Sale> findAllFetchItems();

    @Query("SELECT s FROM Sale s JOIN FETCH s.items WHERE s.id = :id")
    Optional<Sale> findByIdFetchItems(@Param("id") Long id);

    @Query("SELECT s FROM Sale s JOIN FETCH s.items WHERE s.moment BETWEEN :startOfWeek AND :endOfWeek")
    List<Sale> findByWeekRange(@Param("startOfWeek") LocalDateTime startOfWeek, @Param("endOfWeek") LocalDateTime endOfWeek);
}
