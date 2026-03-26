package com.example.quiz.api.dto.quiz;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "QuizDetailsResponse", description = "Complete details of a quiz with all questions and options")
public record QuizDetailsResponse(
        @Schema(description = "Unique identifier of the quiz", example = "1")
        Long id,
        @Schema(description = "Title of the quiz", example = "Java Fundamentals")
        String title,
        @Schema(description = "Description of the quiz", example = "Basic Java programming concepts")
        String description,
        @Schema(description = "List of questions in the quiz")
        List<QuizQuestionResponse> questions,
        @Schema(description = "Start time of the quiz")
        java.time.LocalDateTime startTime,
        @Schema(description = "Time limit of the quiz in minutes")
        Integer timeLimit,
        @Schema(description = "Name of the creator")
        String creatorName
        ) {

}
