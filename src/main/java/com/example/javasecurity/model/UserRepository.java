package com.example.javasecurity.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    // jpa query method
    public User findByUsername(String username);
}
    
