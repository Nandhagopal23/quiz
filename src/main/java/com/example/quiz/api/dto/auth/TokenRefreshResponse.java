package com.example.quiz.api.dto.auth;

public record TokenRefreshResponse(
        String token,
        String refreshToken,
        String tokenType
        ) {

}
