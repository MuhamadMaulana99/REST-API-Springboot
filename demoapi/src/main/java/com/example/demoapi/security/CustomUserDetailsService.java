package com.example.demoapi.security;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Tambahkan ini
import com.example.demoapi.repository.UserRepository;
import com.example.demoapi.model.User;

import java.util.Collections; // Lebih simpel daripada ArrayList manual

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan dengan email: " + email));

        // PENTING: Tambahkan "ROLE_" di depan role dari database agar .hasRole("ADMIN")
        // bekerja
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(authority) // Masukkan authority di sini
        );
    }
}