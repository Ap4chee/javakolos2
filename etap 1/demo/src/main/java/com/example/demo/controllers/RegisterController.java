package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class RegisterController {
    private static final AtomicLong counter = new AtomicLong();
        public RegisterResponse registerUser(){
            long token = counter.incrementAndGet(); //generacja kolejnej liczby całkowitej
            LocalDateTime createdAt = LocalDateTime.now();
            return new RegisterResponse(token, createdAt);
        }
}
