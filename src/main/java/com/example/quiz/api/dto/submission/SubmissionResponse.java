package com.example.quiz.api.dto.submission;

import java.time.LocalDateTime;

public record SubmissionResponse(
        Long submissionId,
        Long quizId,
        Long userId,
        Integer score,
        LocalDateTime submittedAt
        ) {

}
