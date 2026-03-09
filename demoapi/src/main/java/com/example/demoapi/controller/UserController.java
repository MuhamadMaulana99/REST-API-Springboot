package com.example.demoapi.controller;

import com.example.demoapi.dto.AuthResponse;
import com.example.demoapi.dto.RegisterResponse;
import com.example.demoapi.dto.UserRequest;
import com.example.demoapi.dto.UserRequest.LoginRequest;
import com.example.demoapi.model.User;
import com.example.demoapi.repository.UserRepository;
import com.example.demoapi.service.UserLogService;
import com.example.demoapi.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;

import com.example.demoapi.security.JwtService; // Tambahkan ini

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // Tambahkan ini
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.core.Authentication;

// import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLogService logService;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder; // Tambahkan ini
    private final JwtService jwtService; // Tambahkan ini

    // Update Constructor
    public UserController(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Upload foto profil user")
    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            Authentication auth,
            HttpServletRequest request) {

        try {
            String path = userService.saveImage(file);
            userService.updateUserImagePath(auth.getName(), path);

            logService.saveLog(
                    request,
                    auth.getName(),
                    "UPLOAD_IMAGE",
                    null,
                    path);

            return ResponseEntity.ok("File berhasil diupload: " + path);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Gagal upload: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10, sort = "name") Pageable pageable,
            HttpServletRequest request,
            Authentication auth) {

        Page<User> users = userService.getAllUsers(name, pageable);

        logService.saveLog(
                request,
                auth.getName(),
                "GET_USERS",
                null,
                null);

        return ResponseEntity.ok(users);
    }

    // Ini berfungsi sebagai REGISTER
    @PostMapping("/register")
    public RegisterResponse createUser(@RequestBody UserRequest request, HttpServletRequest httpRequest) {
        User user = new User();
        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setEmail(request.getEmail());

        // Enkripsi password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Simpan user (Pastikan addUser mengembalikan object User yang sudah tersimpan)
        User savedUser = userService.addUser(user);

        logService.saveLog(
                httpRequest,
                savedUser.getEmail(),
                "REGISTER_USER",
                null,
                savedUser);

        // Kembalikan response informatif
        return new RegisterResponse(
                "User berhasil didaftarkan!",
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail());
    }

    // Tambahkan Endpoint LOGIN
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        // 1. Cari user
        User user = userService.findByEmail(request.getEmail());

        // 2. Validasi Password
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // 3. Generate Token
            String token = jwtService.generateToken(user.getEmail());

            logService.saveLog(
                    httpRequest,
                    user.getEmail(),
                    "LOGIN",
                    null,
                    null);

            // 4. Kembalikan Object AuthResponse (Otomatis jadi JSON)
            return new AuthResponse(
                    user.getId(), // Pastikan di Model User kamu ada getId()
                    user.getName(),
                    user.getEmail(),
                    user.getRole(),
                    token);
        } else {
            throw new RuntimeException("Password salah!");
        }
    }

    @Operation(summary = "Update data user berdasarkan ID")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable String id, // WAJIB pakai @PathVariable agar id diambil dari URL
            @RequestBody User newDetails, // WAJIB pakai @RequestBody agar muncul kotak JSON di Swagger
            HttpServletRequest request) {

        // Cari user lama
        User oldUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User dengan ID " + id + " tidak ditemukan"));

        // Simpan snapshot data lama untuk log
        User dataSebelumUpdate = new User();
        dataSebelumUpdate.setName(oldUser.getName());
        dataSebelumUpdate.setAge(oldUser.getAge());

        // Update data
        oldUser.setName(newDetails.getName());
        oldUser.setAge(newDetails.getAge());

        if (newDetails.getRole() != null) {
            oldUser.setRole(newDetails.getRole());
        }

        User updatedUser = userRepository.save(oldUser);

        // Logging
        logService.saveLog(request, oldUser.getEmail(), "UPDATE_PROFILE_BY_ID", dataSebelumUpdate, updatedUser);

        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/edit-image")
    public ResponseEntity<?> editImage(
            @RequestParam("file") MultipartFile file,
            Authentication auth,
            HttpServletRequest request) {

        try {
            String path = userService.editUserImage(auth.getName(), file);

            logService.saveLog(
                    request,
                    auth.getName(),
                    "EDIT_IMAGE",
                    null,
                    path);

            return ResponseEntity.ok("Foto profil berhasil diperbarui: " + path);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Gagal memperbarui foto: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-image")
    public ResponseEntity<?> deleteImage(Authentication auth, HttpServletRequest request) {
        try {
            userService.deleteUserImage(auth.getName());

            logService.saveLog(
                    request,
                    auth.getName(),
                    "DELETE_IMAGE",
                    null,
                    null);

            return ResponseEntity.ok("Foto profil berhasil dihapus.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Gagal menghapus foto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id, HttpServletRequest request, Authentication auth) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        userService.deleteUser(id);

        logService.saveLog(
                request,
                auth.getName(),
                "DELETE_USER",
                user,
                null);

        return "Berhasil di hapus";
    }
}