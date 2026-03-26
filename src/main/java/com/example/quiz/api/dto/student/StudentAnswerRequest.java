package com.example.quiz.api.dto.student;

import jakarta.validation.constraints.NotNull;

public record StudentAnswerRequest(
        @NotNull
        Long questionId,
        @NotNull
        Long selectedOptionId
        ) {

}
