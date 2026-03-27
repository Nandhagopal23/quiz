package com.example.quiz.api.dto.teacher;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TeacherQuizSummaryResponse", description = "Summary of a quiz as seen by a teacher")
public record TeacherQuizSummaryResponse(
        @Schema(description = "Quiz ID") Long id,
        @Schema(description = "Quiz title") String title,
        @Schema(description = "Quiz description") String description,
        @Schema(description = "Time limit in minutes") Integer timeLimitMinutes,
        @Schema(description = "Scheduled start time") LocalDateTime startTime,
        @Schema(description = "Number of questions") int questionCount,
        @Schema(description = "Name of the teacher who created the quiz") String createdByName,
        @Schema(description = "When the quiz was created") LocalDateTime createdAt
) {
}
