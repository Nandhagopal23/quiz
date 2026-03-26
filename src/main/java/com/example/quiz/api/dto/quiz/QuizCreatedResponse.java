package com.example.quiz.api.dto.quiz;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "QuizCreatedResponse", description = "Response returned after successfully creating a quiz")
public record QuizCreatedResponse(
        @Schema(description = "Unique identifier of the created quiz", example = "1")
        Long quizId,
        @Schema(description = "Title of the quiz", example = "Java Fundamentals")
        String title,
        @Schema(description = "Total number of questions in the quiz", example = "5")
        int questionCount
        ) {

}
