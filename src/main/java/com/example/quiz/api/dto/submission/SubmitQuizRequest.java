package com.example.quiz.api.dto.submission;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@Schema(name = "SubmitQuizRequest", description = "Request to submit quiz answers")
public record SubmitQuizRequest(
        @NotEmpty
        @Schema(description = "List of answers submitted by the user (at least one required)")
        List<@Valid QuestionAnswerRequest> answers
        ) {

}
