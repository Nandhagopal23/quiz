package com.example.quiz.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "LoginRequest", description = "User login request payload")
public record LoginRequest(
        @NotBlank
        @Email
        @Schema(description = "User's email address", example = "john@example.com")
        String email,
        @NotBlank
        @Schema(description = "User's password", example = "securePassword123")
        String password
        ) {

}
