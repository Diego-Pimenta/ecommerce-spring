package com.compass.ecommerce_spring.repository;

import com.compass.ecommerce_spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
