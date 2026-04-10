package com.enzo.yxzapp.security;

import com.enzo.yxzapp.model.User;
import com.enzo.yxzapp.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas"));

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name()));

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getSenha())     // senha já deve estar em BCrypt hash
                .authorities(authorities)
                .disabled(!u.isAtivo())
                .build();
    }
}