package com.example.demoapi.exception;

import com.example.demoapi.dto.ErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.servlet.http.HttpServletRequest; // Atau javax.servlet... tergantung versi Spring kamu
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.time.LocalDateTime;

// Pastikan kamu mengimpor kelas ErrorResponse yang sudah kamu buat, contoh:
// import com.example.demoapi.dto.ErrorResponse;

// import java.util.HashMap;
// import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handle error spesifik (misal: Bad Request)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    // 2. Handle error validasi (biasanya muncul saat @Valid gagal)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return buildResponseEntity(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex,
            HttpServletRequest request) {
        String message = "File atau halaman tidak ditemukan: " + ex.getResourcePath();
        return buildResponseEntity(HttpStatus.NOT_FOUND, message, request);
    }

    // 3. CATCH-ALL: Handle semua error lain yang tidak terduga (500 Internal Server
    // Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        // Log error ini ke file/database agar kamu bisa debugging!
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi kesalahan pada server", request);
    }

    // Method helper agar kode lebih bersih (DRY - Don't Repeat Yourself)
    private ResponseEntity<ErrorResponse> buildResponseEntity(HttpStatus status, String message,
            HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI());
        return new ResponseEntity<>(errorResponse, status);
    }
}