package com.example.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz.model.QuestionOption;

public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {
}
