package com.personalfinance.accountingsystem.controller;

import com.personalfinance.accountingsystem.dto.ApiResponse;
import com.personalfinance.accountingsystem.dto.AuthRequest;
import com.personalfinance.accountingsystem.dto.AuthResponse;
import com.personalfinance.accountingsystem.dto.RegisterRequest;
import com.personalfinance.accountingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse<?> register(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return userService.login(request);
    }
}
