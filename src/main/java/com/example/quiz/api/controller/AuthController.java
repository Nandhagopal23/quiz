package com.example.quiz.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz.api.dto.auth.AuthResponse;
import com.example.quiz.api.dto.auth.LoginRequest;
import com.example.quiz.api.dto.auth.RefreshTokenRequest;
import com.example.quiz.api.dto.auth.RegisterRequest;
import com.example.quiz.api.dto.auth.TokenRefreshResponse;
import com.example.quiz.service.AuthService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    @SecurityRequirement(name = "bearerAuth")
    public TokenRefreshResponse refreshToken(@Valid @RequestBody RefreshTokenRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return authService.refreshToken(request, userDetails.getUsername());
    }
}
