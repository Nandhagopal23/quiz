package com.example.quiz.api.dto.student;

public record StudentQuizSummaryResponse(
        Long id,
        String title,
        String description,
        Integer timeLimit
        ) {

}
