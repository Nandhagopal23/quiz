package com.example.quiz.api.dto.quiz;

public record QuizCreatedResponse(
        Long quizId,
        String title,
        int questionCount
        ) {

}
