package com.example.quiz.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz.api.dto.auth.AuthResponse;
import com.example.quiz.api.dto.auth.LoginRequest;
import com.example.quiz.api.dto.auth.RegisterRequest;
import com.example.quiz.service.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User registration, login, and token refresh endpoints")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/student/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse registerStudent(@Valid @RequestBody RegisterRequest request) {
        return authService.registerStudent(request);
    }

    @PostMapping("/admin/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse registerAdmin(@Valid @RequestBody RegisterRequest request) {
        return authService.registerAdmin(request);
    }

    @PostMapping("/teacher/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse registerTeacher(@Valid @RequestBody RegisterRequest request) {
        return authService.registerTeacher(request);
    }

    @PostMapping("/student/login")
    public AuthResponse loginStudent(@Valid @RequestBody LoginRequest request) {
        return authService.loginStudent(request);
    }

    @PostMapping("/admin/login")
    public AuthResponse loginAdmin(@Valid @RequestBody LoginRequest request) {
        return authService.loginAdmin(request);
    }

    @PostMapping("/teacher/login")
    public AuthResponse loginTeacher(@Valid @RequestBody LoginRequest request) {
        return authService.loginTeacher(request);
    }
}
