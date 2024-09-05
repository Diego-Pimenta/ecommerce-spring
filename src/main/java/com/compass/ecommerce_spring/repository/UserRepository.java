package com.compass.ecommerce_spring.repository;

import com.compass.ecommerce_spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByCpf(String cpf);

    Optional<User> findByEmail(String email);

    Boolean existsByCpf(String cpf);

    Boolean existsByEmail(String email);
}
