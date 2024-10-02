package com.compass.ecommerce_spring.common;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class UserConstants {

    public static final UserDetails USER_DETAILS = User.withUsername("admin")
            .password("secret")
            .authorities("ADMIN")
            .build();
}
