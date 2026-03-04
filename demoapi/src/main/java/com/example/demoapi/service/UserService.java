package com.example.demoapi.service;

import com.example.demoapi.dto.UserRequest;
import com.example.demoapi.exception.BadRequestException;
import com.example.demoapi.model.User;
import com.example.demoapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.nio.file.*;
import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class UserService {

    private final UserRepository userRepository;

    // constructor injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ➕ CREATE USER
    public User addUser(User user) {
        return userRepository.save(user); // Tambahkan return agar objek yang tersimpan bisa diambil kembali
    }

    // ✅ VALIDASI
    private void validateUser(User user) {

        if (user.getAge() < 18) {
            throw new BadRequestException("Umur harus >= 18");
        }

        if (userRepository.existsByName(user.getName())) {
            throw new BadRequestException("Nama sudah terdaftar");
        }
    }

    public User updateUser(String id, UserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User tidak ditemukan"));

        user.setName(request.getName());
        user.setAge(request.getAge());

        validateUser(user);

        return userRepository.save(user);
    }

    public String saveImage(MultipartFile file) throws IOException {
        // 1. Validasi ekstensi (hanya jpg/png)
        if (file.isEmpty())
            throw new RuntimeException("File kosong!");

        // 2. Generate nama file unik
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // 3. Simpan ke folder uploads
        Path uploadPath = Paths.get("uploads");
        if (!Files.exists(uploadPath))
            Files.createDirectories(uploadPath);

        Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        // 4. Return path yang akan disimpan di DB
        return "uploads/" + fileName;
    }

    public void updateUserImagePath(String email, String path) {
        // 1. Cari user berdasarkan email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        // 2. Update path-nya
        user.setProfileImagePath(path);

        // 3. Simpan perubahan ke DB
        userRepository.save(user);
    }

    public void deleteUser(String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User tidak ditemukan"));

        userRepository.delete(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }

    // 📥 GET ALL USER
    public Page<User> getAllUsers(String name, Pageable pageable) {
        if (name != null && !name.isEmpty()) {
            // Jika ada parameter name, cari dengan filter
            return userRepository.findByNameContainingIgnoreCase(name, pageable);
        }
        // Jika tidak ada parameter name, kembalikan semua data paginated
        return userRepository.findAll(pageable);
    }
}