package com.example.quiz.api.dto.teacher;

import java.time.LocalDateTime;
import java.util.List;

import com.example.quiz.api.dto.quiz.CreateQuestionRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Schema(name = "TeacherCreateQuizRequest", description = "Request to create a quiz with time limit and start time")
public record TeacherCreateQuizRequest(
        @NotBlank
        @Schema(description = "Quiz title", example = "Java Fundamentals")
        String title,

        @Schema(description = "Quiz description", example = "Covers basic Java concepts")
        String description,

        @Min(1)
        @Schema(description = "Time limit in minutes (e.g. 10, 20)", example = "20")
        Integer timeLimitMinutes,

        @Schema(description = "Scheduled start time (ISO-8601). Quiz cannot be modified after this.")
        LocalDateTime startTime,

        @NotEmpty
        @Valid
        @Schema(description = "List of questions (at least one required)")
        List<CreateQuestionRequest> questions
) {
}
