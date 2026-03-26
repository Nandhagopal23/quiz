package com.example.quiz.api.dto.submission;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record SubmitQuizRequest(
        @NotEmpty
        List<@Valid QuestionAnswerRequest> answers
        ) {

}
