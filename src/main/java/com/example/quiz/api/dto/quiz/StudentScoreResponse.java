package com.example.quiz.api.dto.quiz;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "StudentScoreResponse", description = "Student score details for a quiz")
public record StudentScoreResponse(
        @Schema(description = "Student Name", example = "John Doe")
        String studentName,
        @Schema(description = "Student Email", example = "john@example.com")
        String studentEmail,
        @Schema(description = "Score achieved", example = "8")
        Integer score,
        @Schema(description = "Time of submission")
        LocalDateTime submittedAt
        ) {
}
