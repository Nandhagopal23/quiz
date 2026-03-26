package com.example.quiz.api.dto.student;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record StudentSubmitQuizRequest(
        @NotEmpty
        List<@Valid StudentAnswerRequest> answers
        ) {

}
