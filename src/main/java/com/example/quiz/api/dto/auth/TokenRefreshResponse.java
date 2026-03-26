package com.example.quiz.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TokenRefreshResponse", description = "Response containing refreshed JWT tokens")
public record TokenRefreshResponse(
        @Schema(description = "New JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,
        @Schema(description = "New JWT refresh token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String refreshToken,
        @Schema(description = "Token type", example = "Bearer")
        String tokenType
        ) {

}
