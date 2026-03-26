package com.example.quiz.api.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz.api.dto.submission.SubmissionResponse;
import com.example.quiz.api.dto.submission.SubmitQuizRequest;
import com.example.quiz.service.SubmissionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quizzes")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Submissions", description = "Quiz submission and scoring endpoints")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping("/{quizId}/submit")
    @Operation(summary = "Submit quiz answers", description = "Submit user answers for a quiz. Each user can only submit once per quiz. Server-side scoring automatically calculates the score based on correct answers.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz submitted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubmissionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid submission data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),
        @ApiResponse(responseCode = "404", description = "Quiz not found"),
        @ApiResponse(responseCode = "409", description = "User has already submitted this quiz"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public SubmissionResponse submitQuiz(@PathVariable Long quizId,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody SubmitQuizRequest request) {
        return submissionService.submitQuiz(quizId, userDetails.getUsername(), request);
    }
}
