package com.example.quiz.api.dto.teacher;

import java.time.LocalDateTime;
import java.util.List;

import com.example.quiz.api.dto.quiz.CreateQuestionRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@Schema(name = "TeacherUpdateQuizRequest", description = "Request to update an existing quiz (all fields optional)")
public record TeacherUpdateQuizRequest(
        @Schema(description = "New quiz title")
        String title,

        @Schema(description = "New quiz description")
        String description,

        @Min(1)
        @Schema(description = "New time limit in minutes", example = "30")
        Integer timeLimitMinutes,

        @Schema(description = "New start time (ISO-8601)")
        LocalDateTime startTime,

        @Valid
        @Schema(description = "Replacement questions list (replaces all existing questions if provided)")
        List<CreateQuestionRequest> questions
) {
}
