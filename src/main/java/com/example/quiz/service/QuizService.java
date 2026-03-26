package com.example.quiz.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz.api.dto.quiz.CreateOptionRequest;
import com.example.quiz.api.dto.quiz.CreateQuestionRequest;
import com.example.quiz.api.dto.quiz.CreateQuizRequest;
import com.example.quiz.api.dto.quiz.QuizCreatedResponse;
import com.example.quiz.api.dto.quiz.QuizDetailsResponse;
import com.example.quiz.api.dto.quiz.QuizOptionResponse;
import com.example.quiz.api.dto.quiz.QuizQuestionResponse;
import com.example.quiz.exception.ApiException;
import com.example.quiz.model.Question;
import com.example.quiz.model.QuestionOption;
import com.example.quiz.model.Quiz;
import com.example.quiz.repository.QuizRepository;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Transactional
    public QuizCreatedResponse createQuiz(CreateQuizRequest request) {
        Quiz quiz = new Quiz();
        quiz.setTitle(request.title());
        quiz.setDescription(request.description());

        for (CreateQuestionRequest questionRequest : request.questions()) {
            Question question = new Question();
            question.setText(questionRequest.text());
            question.setQuiz(quiz);

            int correctCount = 0;
            for (CreateOptionRequest optionRequest : questionRequest.options()) {
                QuestionOption option = new QuestionOption();
                option.setText(optionRequest.text());
                option.setCorrect(optionRequest.correct());
                option.setQuestion(question);
                question.getOptions().add(option);
                if (optionRequest.correct()) {
                    correctCount++;
                }
            }

            if (correctCount == 0) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Each question must have at least one correct option");
            }
            quiz.getQuestions().add(question);
        }

        Quiz saved = quizRepository.save(quiz);
        return new QuizCreatedResponse(saved.getId(), saved.getTitle(), saved.getQuestions().size());
    }

    @Transactional(readOnly = true)
    public List<QuizDetailsResponse> getAllQuizzes() {
        return quizRepository.findAll().stream().map(this::toDetailsResponse).toList();
    }

    @Transactional(readOnly = true)
    public QuizDetailsResponse getQuizById(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Quiz not found"));
        return toDetailsResponse(quiz);
    }

    private QuizDetailsResponse toDetailsResponse(Quiz quiz) {
        List<QuizQuestionResponse> questions = quiz.getQuestions().stream()
                .map(question -> new QuizQuestionResponse(
                question.getId(),
                question.getText(),
                question.getOptions().stream()
                        .map(option -> new QuizOptionResponse(option.getId(), option.getText()))
                        .toList()
        ))
                .toList();

        return new QuizDetailsResponse(quiz.getId(), quiz.getTitle(), quiz.getDescription(), questions);
    }
}
