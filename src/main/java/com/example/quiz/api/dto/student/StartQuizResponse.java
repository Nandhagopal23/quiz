package com.example.quiz.api.dto.student;

import java.time.LocalDateTime;

public record StartQuizResponse(
        Long submissionId,
        Long userId,
        Long quizId,
        LocalDateTime startTime,
        String status
        ) {

}
