package com.example.quiz.api.dto.submission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "QuestionAnswerRequest", description = "User's answer to a single question")
public record QuestionAnswerRequest(
        @NotNull
        @Schema(description = "The ID of the question being answered", example = "1")
        Long questionId,
        @NotNull
        @Schema(description = "The ID of the selected answer option", example = "1")
        Long selectedOptionId
        ) {

}
