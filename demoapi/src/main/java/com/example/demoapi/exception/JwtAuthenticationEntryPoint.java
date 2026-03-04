package com.example.demoapi.exception;

import com.example.demoapi.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper; // Pastikan import ini
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    // Gunakan constructor injection agar Spring memberikan ObjectMapper yang benar
    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                "Anda harus login terlebih dahulu",
                request.getRequestURI());

        // Gunakan objectMapper yang sudah di-inject (bukan new ObjectMapper())
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}