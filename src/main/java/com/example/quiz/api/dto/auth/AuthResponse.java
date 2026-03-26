package com.example.quiz.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AuthResponse", description = "Authentication response with JWT tokens")
public record AuthResponse(
        @Schema(description = "JWT access token for API authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,
        @Schema(description = "JWT refresh token for obtaining new access tokens", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String refreshToken,
        @Schema(description = "Token type", example = "Bearer")
        String tokenType,
        @Schema(description = "User ID", example = "1")
        Long userId,
        @Schema(description = "User's full name", example = "John Doe")
        String name,
        @Schema(description = "User's email address", example = "john@example.com")
        String email
        ) {

}
