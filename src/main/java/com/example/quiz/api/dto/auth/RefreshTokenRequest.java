package com.example.quiz.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "RefreshTokenRequest", description = "Request to refresh authentication tokens")
public record RefreshTokenRequest(
        @NotBlank
        @Schema(description = "The refresh token obtained from login or previous refresh", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String refreshToken
        ) {

}
