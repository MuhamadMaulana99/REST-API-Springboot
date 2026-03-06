package com.example.demoapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

    private String message;
    private String id;
    private String name;
    private String email;

}