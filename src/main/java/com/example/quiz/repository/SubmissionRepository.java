package com.example.quiz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz.model.Submission;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    Optional<Submission> findByUserIdAndQuizId(Long userId, Long quizId);

    Optional<Submission> findByIdAndUserId(Long submissionId, Long userId);

    boolean existsByUserIdAndQuizId(Long userId, Long quizId);

    java.util.List<Submission> findByQuizId(Long quizId);
}
