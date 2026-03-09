package com.example.demoapi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_logs")
@Data
public class UserLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String action; // LOGIN, REGISTER, UPDATE, DELETE

    private String ipAddress;
    private String userAgent;
    private String device;
    private String browser;
    private String os;

    @Column(columnDefinition = "TEXT")
    private String oldValue; // Data sebelum berubah

    @Column(columnDefinition = "TEXT")
    private String newValue; // Data sesudah berubah

    private LocalDateTime timestamp;
}