package com.example.demoapi.controller;

import com.example.demoapi.model.User;
import com.example.demoapi.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

@PostMapping
public String createUser(@RequestBody User user) {
    userService.addUser(user);
    return "User berhasil ditambahkan";
}

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }
}