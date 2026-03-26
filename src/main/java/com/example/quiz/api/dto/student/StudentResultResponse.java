package com.example.quiz.api.dto.student;

import java.time.LocalDateTime;

public record StudentResultResponse(
        Integer score,
        Integer totalQuestions,
        LocalDateTime submittedAt,
        String status
        ) {

}
