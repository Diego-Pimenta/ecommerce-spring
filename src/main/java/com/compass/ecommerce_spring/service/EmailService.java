package com.compass.ecommerce_spring.service;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendPasswordResetEmail(String toEmail, String subject, String body) throws MessagingException;
}
