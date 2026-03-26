package com.example.quiz.api.dto.student;

import java.time.LocalDateTime;

public record StudentSubmitQuizResponse(
        Long submissionId,
        Integer score,
        String status,
        LocalDateTime submittedAt
        ) {

}
