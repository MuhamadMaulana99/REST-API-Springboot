package com.example.demoapi.service;

import com.example.demoapi.dto.UserRequest;
import com.example.demoapi.exception.BadRequestException;
import com.example.demoapi.model.User;
import com.example.demoapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    // constructor injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ➕ CREATE USER
    public void addUser(User user) {

        validateUser(user);

        userRepository.save(user);
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

    public void deleteUser(String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User tidak ditemukan"));

        userRepository.delete(user);
    }

    // 📥 GET ALL USER
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}