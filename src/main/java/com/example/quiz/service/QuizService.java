package com.example.quiz.service;

import java.time.LocalDateTime;
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
import com.example.quiz.api.dto.quiz.StudentScoreResponse;
import com.example.quiz.api.dto.quiz.UpdateQuizRequest;
import com.example.quiz.exception.ApiException;
import com.example.quiz.model.AppUser;
import com.example.quiz.model.Question;
import com.example.quiz.model.QuestionOption;
import com.example.quiz.model.Quiz;
import com.example.quiz.repository.AppUserRepository;
import com.example.quiz.repository.QuizRepository;
import com.example.quiz.repository.SubmissionRepository;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final AppUserRepository appUserRepository;
    private final SubmissionRepository submissionRepository;

    public QuizService(QuizRepository quizRepository, AppUserRepository appUserRepository, SubmissionRepository submissionRepository) {
        this.quizRepository = quizRepository;
        this.appUserRepository = appUserRepository;
        this.submissionRepository = submissionRepository;
    }

    @Transactional
    public QuizCreatedResponse createQuiz(CreateQuizRequest request, String creatorEmail) {
        AppUser creator = appUserRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        Quiz quiz = new Quiz();
        quiz.setTitle(request.title());
        quiz.setDescription(request.description());
        quiz.setStartTime(request.startTime());
        quiz.setTimeLimit(request.timeLimit());
        quiz.setCreatedBy(creator);

        if (request.questions() != null) {
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

        String creatorName = quiz.getCreatedBy() != null ? quiz.getCreatedBy().getName() : "Unknown";

        return new QuizDetailsResponse(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getDescription(),
                questions,
                quiz.getStartTime(),
                quiz.getTimeLimit(),
                creatorName
        );
    }

    @Transactional
    public void updateQuiz(Long id, UpdateQuizRequest request) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Quiz not found"));

        if (quiz.getStartTime() != null && LocalDateTime.now().isAfter(quiz.getStartTime())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Cannot modify an ongoing or past quiz");
        }

        quiz.setTitle(request.title());
        quiz.setDescription(request.description());
        quiz.setStartTime(request.startTime());
        quiz.setTimeLimit(request.timeLimit());
        
        if (request.questions() != null && !request.questions().isEmpty()) {
            quiz.getQuestions().clear();
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
        }
    }

    @Transactional
    public void deleteQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Quiz not found"));

        if (quiz.getStartTime() != null && LocalDateTime.now().isAfter(quiz.getStartTime())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Cannot delete an ongoing or past quiz");
        }

        quizRepository.delete(quiz);
    }

    @Transactional(readOnly = true)
    public List<StudentScoreResponse> getQuizScores(Long quizId) {
        return submissionRepository.findByQuizId(quizId).stream()
                .map(submission -> new StudentScoreResponse(
                        submission.getUser().getName(),
                        submission.getUser().getEmail(),
                        submission.getScore(),
                        submission.getSubmittedAt()
                ))
                .toList();
    }
}
