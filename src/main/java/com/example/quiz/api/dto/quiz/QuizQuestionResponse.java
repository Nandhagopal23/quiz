package com.example.quiz.api.dto.quiz;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "QuizQuestionResponse", description = "A question within a quiz")
public record QuizQuestionResponse(
        @Schema(description = "Unique identifier of the question", example = "1")
        Long id,
        @Schema(description = "The question text", example = "What is the capital of France?")
        String text,
        @Schema(description = "List of answer options for this question")
        List<QuizOptionResponse> options
        ) {

}
