package com.compass.ecommerce_spring.repository;

import com.compass.ecommerce_spring.entity.PasswordResetToken;
import com.compass.ecommerce_spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUser(User user);
}
