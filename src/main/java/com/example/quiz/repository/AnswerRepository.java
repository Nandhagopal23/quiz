package com.example.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz.model.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
