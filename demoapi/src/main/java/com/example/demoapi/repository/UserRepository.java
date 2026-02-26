package com.example.demoapi.repository;

import com.example.demoapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByName(String name);

}