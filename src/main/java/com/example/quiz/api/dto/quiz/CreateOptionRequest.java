package com.example.quiz.api.dto.quiz;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "CreateOptionRequest", description = "Request to add an answer option to a question")
public record CreateOptionRequest(
        @NotBlank
        @Schema(description = "The option text", example = "Paris")
        String text,
        @Schema(description = "Whether this is the correct answer", example = "true")
        boolean correct
        ) {

}
