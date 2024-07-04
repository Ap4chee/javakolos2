package com.example.demo.controllers;

import java.time.LocalDateTime;

public class RegisterResponse {
    private long token;
    private LocalDateTime createdAt;

    public RegisterResponse(long token, LocalDateTime createdAt) {
        this.token = token;
        this.createdAt = createdAt;
    }
    public long getToken() {
        return token;
    }
    public void setToken(long token) {
        this.token = token;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
