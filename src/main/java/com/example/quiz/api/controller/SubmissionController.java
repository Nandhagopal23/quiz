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

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quizzes")
@SecurityRequirement(name = "bearerAuth")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping("/{quizId}/submit")
    public SubmissionResponse submitQuiz(@PathVariable Long quizId,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody SubmitQuizRequest request) {
        return submissionService.submitQuiz(quizId, userDetails.getUsername(), request);
    }
}
