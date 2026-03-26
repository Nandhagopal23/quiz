package com.example.quiz.api.dto.student;

import java.util.List;

public record StudentQuizDetailsResponse(
        Long quizId,
        String title,
        List<StudentQuizQuestionResponse> questions
        ) {

}
