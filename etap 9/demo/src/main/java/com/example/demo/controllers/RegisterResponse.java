package com.example.demo.controllers;

import java.time.LocalDateTime;

public class RegisterResponse {
    private String token;
    private LocalDateTime createdAt;

    public RegisterResponse(String token, LocalDateTime createdAt) {
        this.token = token;
        this.createdAt = createdAt;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
