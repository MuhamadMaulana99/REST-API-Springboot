package com.example.demoapi.controller;

import com.example.demoapi.dto.AuthResponse;
import com.example.demoapi.dto.RegisterResponse;
import com.example.demoapi.dto.UserRequest;
import com.example.demoapi.dto.UserRequest.LoginRequest;
import com.example.demoapi.model.User;
import com.example.demoapi.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;

import com.example.demoapi.security.JwtService; // Tambahkan ini
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
            @Parameter(description = "File gambar (jpg/png) maks 2MB") @RequestParam("file") MultipartFile file,
            Authentication auth) {

        try {
            String path = userService.saveImage(file);
            userService.updateUserImagePath(auth.getName(), path);
            return ResponseEntity.ok("File berhasil diupload: " + path);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Gagal upload: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(name, pageable));
    }

    // Ini berfungsi sebagai REGISTER
    @PostMapping("/register")
    public RegisterResponse createUser(@RequestBody UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setEmail(request.getEmail());

        // Enkripsi password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Simpan user (Pastikan addUser mengembalikan object User yang sudah tersimpan)
        User savedUser = userService.addUser(user);

        // Kembalikan response informatif
        return new RegisterResponse(
                "User berhasil didaftarkan!",
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail());
    }

    // Tambahkan Endpoint LOGIN
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        // 1. Cari user
        User user = userService.findByEmail(request.getEmail());

        // 2. Validasi Password
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // 3. Generate Token
            String token = jwtService.generateToken(user.getEmail());

            // 4. Kembalikan Object AuthResponse (Otomatis jadi JSON)
            return new AuthResponse(
                    user.getId(), // Pastikan di Model User kamu ada getId()
                    user.getName(),
                    user.getEmail(),
                    token);
        } else {
            throw new RuntimeException("Password salah!");
        }
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @RequestBody UserRequest request) {
        return userService.updateUser(id, request);
    }

    @PutMapping("/edit-image")
    public ResponseEntity<?> editImage(@RequestParam("file") MultipartFile file, Authentication auth) {
        try {
            String path = userService.editUserImage(auth.getName(), file);
            return ResponseEntity.ok("Foto profil berhasil diperbarui: " + path);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Gagal memperbarui foto: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-image")
    public ResponseEntity<?> deleteImage(Authentication auth) {
        try {
            // auth.getName() biasanya berisi email/username dari token JWT
            userService.deleteUserImage(auth.getName());
            return ResponseEntity.ok("Foto profil berhasil dihapus.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Gagal menghapus foto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return "Berhasil di hapus";
    }
}