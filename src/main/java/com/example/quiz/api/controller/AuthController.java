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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new user", description = "Create a new user account with email and password. Returns JWT access and refresh tokens.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or email already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user with email and password. Returns JWT access and refresh tokens.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid email or password"),
        @ApiResponse(responseCode = "401", description = "Authentication failed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Refresh authentication tokens", description = "Generate new access and refresh tokens using an existing refresh token. Requires bearer token authentication.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tokens refreshed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenRefreshResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid or expired refresh token"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public TokenRefreshResponse refreshToken(@Valid @RequestBody RefreshTokenRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return authService.refreshToken(request, userDetails.getUsername());
    }
}
