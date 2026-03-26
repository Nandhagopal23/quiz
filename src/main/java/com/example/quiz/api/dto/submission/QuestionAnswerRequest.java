package com.example.quiz.api.dto.submission;

import jakarta.validation.constraints.NotNull;

public record QuestionAnswerRequest(
        @NotNull
        Long questionId,
        @NotNull
        Long selectedOptionId
        ) {

}
