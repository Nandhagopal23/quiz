package com.example.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
