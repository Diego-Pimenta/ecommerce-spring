package com.compass.ecommerce_spring.security;

import com.compass.ecommerce_spring.entity.enums.Role;
import com.compass.ecommerce_spring.exception.custom.BusinessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AccessAuthority {

    public String retrieveUserCpfFromToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    public Role retrieveUserRoleFromToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetails) authentication.getPrincipal();

        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(Role::valueOf)
                .findFirst()
                .orElseThrow();
    }

    public void checkPermission(String cpf) {
        // somente admin pode acessar/alterar informações de outros usuários
        if (retrieveUserRoleFromToken().equals(Role.CLIENT) && !retrieveUserCpfFromToken().equals(cpf)) {
            throw new BusinessException("Insufficient permissions to perform action");
        }
    }
}
