package com.example.quiz.api.dto.quiz;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateQuizRequest(
        @NotBlank
        String title,
        String description,
        @NotEmpty
        List<@Valid CreateQuestionRequest> questions
        ) {

}
