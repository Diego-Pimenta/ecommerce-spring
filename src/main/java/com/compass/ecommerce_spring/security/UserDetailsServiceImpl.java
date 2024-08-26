package com.compass.ecommerce_spring.security;

import com.compass.ecommerce_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        var user = repository.findByCpf(cpf)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var grantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()));

        return new User(user.getCpf(), user.getPassword(), grantedAuthorities);
    }
}
