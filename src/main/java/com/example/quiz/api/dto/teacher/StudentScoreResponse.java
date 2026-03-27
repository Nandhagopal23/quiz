package com.example.quiz.api.dto.teacher;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "StudentScoreResponse", description = "Score of a single student for a quiz")
public record StudentScoreResponse(
        @Schema(description = "Student's user ID") Long studentId,
        @Schema(description = "Student's name") String studentName,
        @Schema(description = "Student's email") String studentEmail,
        @Schema(description = "Score achieved") Integer score,
        @Schema(description = "Submission status (IN_PROGRESS, SUBMITTED)") String status,
        @Schema(description = "When the student submitted") LocalDateTime submittedAt
) {
}
