package com.example.quiz.api.dto.student;

import java.util.List;

public record StudentQuizQuestionResponse(
        Long questionId,
        String text,
        List<StudentQuizOptionResponse> options
        ) {

}
