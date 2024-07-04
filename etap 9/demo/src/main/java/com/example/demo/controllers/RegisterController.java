package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

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
    public boolean verifyToken(String token) {
        if (tokenManager.isTokenValid(token)) {
            return true;
        } else {
            return false;
        }
    }
    @GetMapping("/tokens")
    public List<TokenInfo> getAllTokens(){
        return tokenManager.getAllTokens();
    }
}
