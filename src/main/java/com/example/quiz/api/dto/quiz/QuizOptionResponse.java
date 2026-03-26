package com.example.quiz.api.dto.quiz;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "QuizOptionResponse", description = "An answer option for a quiz question")
public record QuizOptionResponse(
        @Schema(description = "Unique identifier of the option", example = "1")
        Long id,
        @Schema(description = "The option text", example = "Paris")
        String text
        ) {

}
