package com.example.demoapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String id;
    private String name;
    private String email;
    private String token;

}