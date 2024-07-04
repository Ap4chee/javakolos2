package com.example.demo.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class RegisterController {
    private final TokenManager tokenManager = new TokenManager();

    @PostMapping("/register")
    public RegisterResponse registerUser() {
        String token = tokenManager.generateToken();
        LocalDateTime createdAt = LocalDateTime.now();
        return new RegisterResponse(token, createdAt);
    }

    @PostMapping("/verify")
    public String verifyToken(String token) {
        if (tokenManager.isTokenValid(token)) {
            return "Token is valid";
        } else {
            return "Token is invalid";
        }
    }
}
