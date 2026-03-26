package com.example.quiz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz.model.QuestionOption;

public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {

    Optional<QuestionOption> findByIdAndQuestionId(Long id, Long questionId);
}
