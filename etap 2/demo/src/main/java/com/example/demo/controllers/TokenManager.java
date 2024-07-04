package com.example.demo.controllers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class TokenManager {

    private Map<String, LocalDateTime> tokenMap = new HashMap<>();
    private long nextId = 1;
    private long tokenValidityMinutes = 5; // Czas ważności tokena w minutach

    public String generateToken() {
        String token = "token" + nextId++;
        LocalDateTime createdAt = LocalDateTime.now();
        tokenMap.put(token, createdAt);
        return token;
    }

    public LocalDateTime getTokenCreationTime(String token) {
        return tokenMap.get(token);
    }

    public boolean isTokenValid(String token) {
        LocalDateTime createdAt = tokenMap.get(token);
        if (createdAt == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.MINUTES.between(createdAt, now) <= tokenValidityMinutes;
    }
}
