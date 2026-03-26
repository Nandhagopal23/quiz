package com.example.quiz.api.dto.submission;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SubmissionResponse", description = "Response confirming quiz submission and score")
public record SubmissionResponse(
        @Schema(description = "Unique identifier of the submission", example = "1")
        Long submissionId,
        @Schema(description = "ID of the quiz that was submitted", example = "1")
        Long quizId,
        @Schema(description = "ID of the user who submitted the quiz", example = "1")
        Long userId,
        @Schema(description = "Score obtained (number of correct answers)", example = "4")
        Integer score,
        @Schema(description = "Timestamp when the quiz was submitted", example = "2026-03-26T15:30:00")
        LocalDateTime submittedAt
        ) {

}
