package com.example.demoapi.controller;

import com.example.demoapi.model.UserLog;
import com.example.demoapi.repository.UserLogRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
@Tag(name = "Audit Log", description = "Endpoint untuk monitoring perilaku user")
public class LogController {

    @Autowired
    private UserLogRepository logRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Ambil semua history log (Urut terbaru)")
    public ResponseEntity<List<UserLog>> getAllLogs() {
        return ResponseEntity.ok(logRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp")));
    }
}