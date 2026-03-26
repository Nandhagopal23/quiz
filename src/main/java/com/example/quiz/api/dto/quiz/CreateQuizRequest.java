package com.example.quiz.api.dto.quiz;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Schema(name = "CreateQuizRequest", description = "Request to create a new quiz with questions and options")
public record CreateQuizRequest(
        @NotBlank
        @Schema(description = "Quiz title", example = "Java Fundamentals")
        String title,
        @Schema(description = "Quiz description", example = "Basic Java programming concepts")
        String description,
        @NotEmpty
        @Schema(description = "List of questions for the quiz (at least one required)")
        List<@Valid CreateQuestionRequest> questions
        ) {

}
