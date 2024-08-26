package com.compass.ecommerce_spring.security;

import com.compass.ecommerce_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = repository.findByCpf(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var grantedAuthorities = List.of(new SimpleGrantedAuthority(user.getRole().toString()));

        return new User(user.getCpf(), user.getPassword(), grantedAuthorities);
    }
}
