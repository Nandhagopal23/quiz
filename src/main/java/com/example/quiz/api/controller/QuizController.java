package com.example.quiz.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz.api.dto.quiz.CreateQuizRequest;
import com.example.quiz.api.dto.quiz.QuizCreatedResponse;
import com.example.quiz.api.dto.quiz.QuizDetailsResponse;
import com.example.quiz.service.QuizService;

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
@Tag(name = "Quizzes", description = "Quiz CRUD operations and retrieval endpoints")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new quiz", description = "Create a new quiz with questions and answer options. Questions must include at least one correct option.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Quiz created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = QuizCreatedResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid quiz data or validation failed"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public QuizCreatedResponse createQuiz(@Valid @RequestBody CreateQuizRequest request) {
        return quizService.createQuiz(request);
    }

    @GetMapping
    @Operation(summary = "List all quizzes", description = "Retrieve a list of all available quizzes with their basic details.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quizzes retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = QuizDetailsResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<QuizDetailsResponse> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @GetMapping("/{quizId}")
    @Operation(summary = "Get quiz details", description = "Retrieve detailed information about a specific quiz including all questions and answer options (correct answers are hidden).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = QuizDetailsResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),
        @ApiResponse(responseCode = "404", description = "Quiz not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public QuizDetailsResponse getQuizById(@PathVariable Long quizId) {
        return quizService.getQuizById(quizId);
    }
}
