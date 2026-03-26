package com.example.quiz.api.dto.quiz;

import java.util.List;

public record QuizDetailsResponse(
        Long id,
        String title,
        String description,
        List<QuizQuestionResponse> questions
        ) {

}
