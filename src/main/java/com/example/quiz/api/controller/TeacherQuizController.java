package com.example.quiz.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz.api.dto.quiz.CreateQuizRequest;
import com.example.quiz.api.dto.quiz.QuizCreatedResponse;
import com.example.quiz.api.dto.quiz.QuizDetailsResponse;
import com.example.quiz.api.dto.quiz.StudentScoreResponse;
import com.example.quiz.api.dto.quiz.UpdateQuizRequest;
import com.example.quiz.service.QuizService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/teacher/quizzes")
@Tag(name = "Teacher Quiz Management", description = "Endpoints for teachers to manage quizzes")
@SecurityRequirement(name = "Bearer Authentication")
public class TeacherQuizController {

    private final QuizService quizService;

    public TeacherQuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    @Operation(summary = "Create a new quiz")
    public ResponseEntity<QuizCreatedResponse> createQuiz(@Valid @RequestBody CreateQuizRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        QuizCreatedResponse response = quizService.createQuiz(request, currentPrincipalName);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all ongoing quizzes created by teachers")
    public ResponseEntity<List<QuizDetailsResponse>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modify a quiz within the start time")
    public ResponseEntity<Void> updateQuiz(@PathVariable Long id, @Valid @RequestBody UpdateQuizRequest request) {
        quizService.updateQuiz(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a quiz within the start time")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/scores")
    @Operation(summary = "Get scores of students who submitted the quiz")
    public ResponseEntity<List<StudentScoreResponse>> getQuizScores(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getQuizScores(id));
    }
}
