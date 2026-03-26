package com.example.quiz.api.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz.api.dto.student.StartQuizResponse;
import com.example.quiz.api.dto.student.StudentQuizDetailsResponse;
import com.example.quiz.api.dto.student.StudentQuizSummaryResponse;
import com.example.quiz.api.dto.student.StudentResultResponse;
import com.example.quiz.api.dto.student.StudentSubmitQuizRequest;
import com.example.quiz.api.dto.student.StudentSubmitQuizResponse;
import com.example.quiz.service.StudentQuizService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/student")
public class StudentQuizController {

    private final StudentQuizService studentQuizService;

    public StudentQuizController(StudentQuizService studentQuizService) {
        this.studentQuizService = studentQuizService;
    }

    @GetMapping("/quizzes")
    public List<StudentQuizSummaryResponse> getAllQuizzes() {
        return studentQuizService.getAllQuizzes();
    }

    @GetMapping("/quizzes/{quizId}")
    public StudentQuizDetailsResponse getQuizDetails(@PathVariable Long quizId) {
        return studentQuizService.getQuizDetails(quizId);
    }

    @PostMapping("/quizzes/{quizId}/start")
    public StartQuizResponse startQuiz(@PathVariable Long quizId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return studentQuizService.startQuiz(quizId, userDetails.getUsername());
    }

    @PostMapping("/quizzes/{quizId}/submit")
    public StudentSubmitQuizResponse submitQuiz(@PathVariable Long quizId,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody StudentSubmitQuizRequest request) {
        return studentQuizService.submitQuiz(quizId, userDetails.getUsername(), request);
    }

    @GetMapping("/submissions/{submissionId}")
    public StudentResultResponse getResult(@PathVariable Long submissionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return studentQuizService.getResult(submissionId, userDetails.getUsername());
    }
}
