package com.example.demo.controllers;

import java.time.LocalDateTime;

public class TokenInfo {

    private String token;
    private LocalDateTime createdAt;
    private boolean isValid;

    public TokenInfo(String token, LocalDateTime createdAt, boolean isValid) {
        this.token = token;
        this.createdAt = createdAt;
        this.isValid = isValid;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isValid() {
        return isValid;
    }
}
