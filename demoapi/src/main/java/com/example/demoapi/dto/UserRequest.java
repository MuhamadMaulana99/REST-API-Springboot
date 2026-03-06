package com.example.demoapi.dto;

import lombok.Data;

@Data
public class UserRequest {

    private String name;
    private String email;
    private Integer age;
    private String password;

    // constructor kosong (untuk JSON mapping)
    public UserRequest() {
    }

    // constructor dengan parameter
    public UserRequest(String name, String email, String password, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.password = password;
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }
}