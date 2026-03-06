package com.example.demoapi.dto;

import lombok.Data;

public class UserRequest {

    private String name;
    private String email;
    private Integer age;
    private String password;

    // constructor kosong (WAJIB untuk JSON mapping)
    public UserRequest() {
    }

    @Data
    public static class LoginRequest { // Tambahkan STATIC di sini
        private String email;
        private String password;
    }

    // constructor dengan parameter
    public UserRequest(String name, String email, String password, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.password = password;
    }

    // getter & setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}