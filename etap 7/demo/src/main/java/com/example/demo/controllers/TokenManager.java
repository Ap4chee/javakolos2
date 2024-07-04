package com.example.demo.controllers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TokenManager {

    private List<TokenInfo> tokenList = new ArrayList<>();
    private long nextId = 1;
    private long tokenValidityMinutes = 5; // Czas ważności tokena w minutach

    public String generateToken() {
        String token = "token" + nextId++;
        LocalDateTime createdAt = LocalDateTime.now();
        TokenInfo tokenInfo = new TokenInfo(token, createdAt, true); // Domyślnie uznajemy token za ważny
        tokenList.add(tokenInfo);
        return token;
    }

    public LocalDateTime getTokenCreationTime(String token) {
        TokenInfo tokenInfo = getTokenInfo(token);
        if (tokenInfo != null) {
            return tokenInfo.getCreatedAt();
        }
        return null;
    }

    public boolean isTokenValid(String token) {
        LocalDateTime createdAt = getTokenCreationTime(token);
        if (createdAt != null) {
            LocalDateTime now = LocalDateTime.now();
            return ChronoUnit.MINUTES.between(createdAt, now) <= tokenValidityMinutes;
        }
        return false;
    }

    public List<TokenInfo> getAllTokens() {
        // Usuwanie nieaktualnych tokenów
        LocalDateTime now = LocalDateTime.now();
        Iterator<TokenInfo> iterator = tokenList.iterator();
        while (iterator.hasNext()) {
            TokenInfo tokenInfo = iterator.next();
            LocalDateTime createdAt = tokenInfo.getCreatedAt();
            if (!createdAt.isAfter(now.minusMinutes(tokenValidityMinutes))) {
                iterator.remove(); // Usunięcie nieaktualnego tokenu z listy
            }
        }
        return tokenList;
    }

    private TokenInfo getTokenInfo(String token) {
        for (TokenInfo tokenInfo : tokenList) {
            if (tokenInfo.getToken().equals(token)) {
                return tokenInfo;
            }
        }
        return null;
    }
}
