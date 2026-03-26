package com.example.quiz.api.dto.auth;

public record AuthResponse(
        String token,
        String refreshToken,
        String tokenType,
        Long userId,
        String name,
        String email
        ) {

}
