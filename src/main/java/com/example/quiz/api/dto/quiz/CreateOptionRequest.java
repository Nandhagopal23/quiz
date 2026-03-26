package com.example.quiz.api.dto.quiz;

import jakarta.validation.constraints.NotBlank;

public record CreateOptionRequest(
        @NotBlank
        String text,
        boolean correct
        ) {

}
