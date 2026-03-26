package com.example.quiz.api.dto.quiz;

import java.util.List;

public record QuizQuestionResponse(
        Long id,
        String text,
        List<QuizOptionResponse> options
        ) {

}
