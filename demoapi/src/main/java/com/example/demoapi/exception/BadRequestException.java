package com.example.demoapi.exception;

// Simpan di package: com.example.demoapi.exception
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}