package com.example.demoapi.dto;

public class RegisterResponse {
    private String message;
    private String id;
    private String name;
    private String email;

    public RegisterResponse(String message, String id, String name, String email) {
        this.message = message;
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getter & Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
}