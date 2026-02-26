package com.example.demoapi.controller;

import com.example.demoapi.dto.UserRequest;
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

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public String createUser(@RequestBody UserRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setAge(request.getAge());

        userService.addUser(user);

        return "User berhasil ditambahkan";
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id,
            @RequestBody UserRequest request) {

        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return id;
    }

}