package com.example.quiz.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "RegisterRequest", description = "User registration request payload")
public record RegisterRequest(
        @NotBlank
        @Size(max = 100)
        @Schema(description = "User's full name", example = "John Doe", maxLength = 100)
        String name,
        @NotBlank
        @Email
        @Size(max = 100)
        @Schema(description = "User's email address (must be unique)", example = "john@example.com", maxLength = 100)
        String email,
        @NotBlank
        @Size(min = 6, max = 120)
        @Schema(description = "User's password (minimum 6 characters)", example = "securePassword123", minLength = 6, maxLength = 120)
        String password
        ) {

}
