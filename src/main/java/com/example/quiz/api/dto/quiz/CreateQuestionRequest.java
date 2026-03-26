package com.example.quiz.api.dto.quiz;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Schema(name = "CreateQuestionRequest", description = "Request to add a question to a quiz")
public record CreateQuestionRequest(
        @NotBlank
        @Schema(description = "The question text", example = "What is the capital of France?")
        String text,
        @NotEmpty
        @Schema(description = "List of answer options for this question (at least two recommended)")
        List<@Valid CreateOptionRequest> options
        ) {

}
