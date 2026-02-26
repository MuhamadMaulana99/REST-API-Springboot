package com.example.demoapi.service;

import com.example.demoapi.exception.BadRequestException;
import com.example.demoapi.model.User;
import com.example.demoapi.repository.UserRepository;
import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // @Transactional // Pastikan database konsisten
    public void addUser(User user) {
        // 1. Validasi (Fail Fast Principle)
        validateUser(user);

        // 2. Persiapan Data
        user.setId(UUID.randomUUID().toString());

        // 3. Eksekusi
        userRepository.save(user);
    }

    private void validateUser(User user) {
        if (user.getAge() < 18) {
            throw new BadRequestException("Umur harus >= 18");
        }

        if (userRepository.existsByName(user.getName())) {
            throw new BadRequestException("Nama sudah terdaftar");
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}