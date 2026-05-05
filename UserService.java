package com.personalfinance.accountingsystem.service;

import com.personalfinance.accountingsystem.dto.ApiResponse;
import com.personalfinance.accountingsystem.dto.AuthRequest;
import com.personalfinance.accountingsystem.dto.AuthResponse;
import com.personalfinance.accountingsystem.dto.RegisterRequest;
import com.personalfinance.accountingsystem.entity.User;
import com.personalfinance.accountingsystem.repository.UserRepository;
import com.personalfinance.accountingsystem.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public ApiResponse<?> register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ApiResponse.error("Username already exists");
        }
        User user = new User(request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail());
        userRepository.save(user);
        return ApiResponse.success("Registration successful", null);
    }

    public ApiResponse<AuthResponse> login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = tokenProvider.generateToken(user.getId(), user.getUsername());
        return ApiResponse.success(new AuthResponse(token, user.getId(), user.getUsername()));
    }
}
