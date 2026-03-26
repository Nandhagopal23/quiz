package com.example.quiz.api.dto.quiz;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@Schema(name = "UpdateQuizRequest", description = "Request to update an existing quiz")
public record UpdateQuizRequest(
        @Schema(description = "Quiz title", example = "Java Fundamentals")
        String title,
        @Schema(description = "Quiz description", example = "Basic Java programming concepts")
        String description,
        @Schema(description = "Start time of the quiz")
        LocalDateTime startTime,
        @Schema(description = "Time limit of the quiz in minutes")
        Integer timeLimit,
        @Schema(description = "List of questions to update (optional)")
        List<@Valid CreateQuestionRequest> questions
        ) {
}
