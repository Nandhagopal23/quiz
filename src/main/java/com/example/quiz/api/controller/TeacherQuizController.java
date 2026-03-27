package com.example.quiz.api.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz.api.dto.teacher.StudentScoreResponse;
import com.example.quiz.api.dto.teacher.TeacherCreateQuizRequest;
import com.example.quiz.api.dto.teacher.TeacherQuizSummaryResponse;
import com.example.quiz.api.dto.teacher.TeacherUpdateQuizRequest;
import com.example.quiz.service.TeacherQuizService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Teacher - Quiz Management", description = "Endpoints for teachers to manage quizzes and view scores")
@RestController
@RequestMapping("/api/teacher/quizzes")
public class TeacherQuizController {

    private final TeacherQuizService teacherQuizService;

    public TeacherQuizController(TeacherQuizService teacherQuizService) {
        this.teacherQuizService = teacherQuizService;
    }

    @Operation(summary = "Create a new quiz",
            description = "Create a quiz with title, description, time limit, start time, and questions. Requires ROLE_TEACHER.")
    @PostMapping
    public ResponseEntity<TeacherQuizSummaryResponse> createQuiz(
            @Valid @RequestBody TeacherCreateQuizRequest request,
            Principal principal) {
        TeacherQuizSummaryResponse response = teacherQuizService.createQuiz(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "List all quizzes",
            description = "Returns all quizzes created by any teacher. Requires ROLE_TEACHER.")
    @GetMapping
    public ResponseEntity<List<TeacherQuizSummaryResponse>> getAllQuizzes() {
        return ResponseEntity.ok(teacherQuizService.getAllQuizzes());
    }

    @Operation(summary = "Update a quiz",
            description = "Update quiz fields. Only allowed before the quiz's startTime. The teacher must own the quiz.")
    @PutMapping("/{id}")
    public ResponseEntity<TeacherQuizSummaryResponse> updateQuiz(
            @PathVariable Long id,
            @Valid @RequestBody TeacherUpdateQuizRequest request,
            Principal principal) {
        return ResponseEntity.ok(teacherQuizService.updateQuiz(id, request, principal.getName()));
    }

    @Operation(summary = "Delete a quiz",
            description = "Delete a quiz. Only allowed before the quiz's startTime. The teacher must own the quiz.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(
            @PathVariable Long id,
            Principal principal) {
        teacherQuizService.deleteQuiz(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get student scores for a quiz",
            description = "Returns a list of scores for each student who attempted this quiz. Requires ROLE_TEACHER.")
    @GetMapping("/{id}/scores")
    public ResponseEntity<List<StudentScoreResponse>> getStudentScores(@PathVariable Long id) {
        return ResponseEntity.ok(teacherQuizService.getStudentScores(id));
    }
}
