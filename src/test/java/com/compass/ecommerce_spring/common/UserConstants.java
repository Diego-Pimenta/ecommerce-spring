package com.compass.ecommerce_spring.common;

import com.compass.ecommerce_spring.dto.request.AuthenticationRequestDto;
import com.compass.ecommerce_spring.entity.enums.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

public class UserConstants {

    public static final User USER_DETAILS = new User("admin", "secret", Collections.singleton(new SimpleGrantedAuthority(Role.ADMIN.name())));

    public static final AuthenticationRequestDto AUTHENTICATION_REQUEST_DTO = new AuthenticationRequestDto("12345678901", "adminpass");
}
