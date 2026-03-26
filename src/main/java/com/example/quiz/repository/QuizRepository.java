package com.example.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz.model.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
