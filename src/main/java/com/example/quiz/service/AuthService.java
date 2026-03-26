package com.example.quiz.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.quiz.api.dto.auth.AuthResponse;
import com.example.quiz.api.dto.auth.LoginRequest;
import com.example.quiz.api.dto.auth.RegisterRequest;
import com.example.quiz.exception.ApiException;
import com.example.quiz.model.AppUser;
import com.example.quiz.repository.AppUserRepository;
import com.example.quiz.security.JwtService;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse registerStudent(RegisterRequest request) {
        return registerWithRole(request, "ROLE_STUDENT");
    }

    public AuthResponse registerAdmin(RegisterRequest request) {
        return registerWithRole(request, "ROLE_ADMIN");
    }

    public AuthResponse registerTeacher(RegisterRequest request) {
        return registerWithRole(request, "ROLE_TEACHER");
    }

    private AuthResponse registerWithRole(RegisterRequest request, String role) {
        if (appUserRepository.findByEmail(request.email()).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "Email is already registered");
        }

        AppUser appUser = new AppUser();
        appUser.setName(request.name());
        appUser.setEmail(request.email().toLowerCase());
        appUser.setPassword(passwordEncoder.encode(request.password()));
        appUser.setRole(role);

        AppUser saved = appUserRepository.save(appUser);
        UserDetails securityUser = User.withUsername(saved.getEmail())
                .password(saved.getPassword())
                .authorities(saved.getRole())
                .build();

        String token = jwtService.generateToken(securityUser);
        String refreshToken = jwtService.generateRefreshToken(securityUser);
        return new AuthResponse(token, refreshToken, "Bearer", saved.getId(), saved.getName(), saved.getEmail());
    }

    public AuthResponse loginStudent(LoginRequest request) {
        return loginWithRole(request, "ROLE_STUDENT");
    }

    public AuthResponse loginAdmin(LoginRequest request) {
        return loginWithRole(request, "ROLE_ADMIN");
    }

    public AuthResponse loginTeacher(LoginRequest request) {
        return loginWithRole(request, "ROLE_TEACHER");
    }

    private AuthResponse loginWithRole(LoginRequest request, String expectedRole) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        AppUser appUser = appUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!expectedRole.equals(appUser.getRole())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "User does not have required role");
        }

        UserDetails securityUser = User.withUsername(appUser.getEmail())
                .password(appUser.getPassword())
                .authorities(appUser.getRole())
                .build();

        String token = jwtService.generateToken(securityUser);
        String refreshToken = jwtService.generateRefreshToken(securityUser);
        return new AuthResponse(token, refreshToken, "Bearer", appUser.getId(), appUser.getName(), appUser.getEmail());
    }
}
