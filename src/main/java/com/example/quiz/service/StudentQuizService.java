package com.example.quiz.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz.api.dto.student.StartQuizResponse;
import com.example.quiz.api.dto.student.StudentAnswerRequest;
import com.example.quiz.api.dto.student.StudentQuizDetailsResponse;
import com.example.quiz.api.dto.student.StudentQuizOptionResponse;
import com.example.quiz.api.dto.student.StudentQuizQuestionResponse;
import com.example.quiz.api.dto.student.StudentQuizSummaryResponse;
import com.example.quiz.api.dto.student.StudentResultResponse;
import com.example.quiz.api.dto.student.StudentSubmitQuizRequest;
import com.example.quiz.api.dto.student.StudentSubmitQuizResponse;
import com.example.quiz.exception.ApiException;
import com.example.quiz.model.Answer;
import com.example.quiz.model.AppUser;
import com.example.quiz.model.QuestionOption;
import com.example.quiz.model.Quiz;
import com.example.quiz.model.Submission;
import com.example.quiz.model.SubmissionStatus;
import com.example.quiz.repository.AnswerRepository;
import com.example.quiz.repository.AppUserRepository;
import com.example.quiz.repository.QuestionOptionRepository;
import com.example.quiz.repository.QuizRepository;
import com.example.quiz.repository.SubmissionRepository;

@Service
public class StudentQuizService {

    private static final int DEFAULT_TIME_LIMIT_MINUTES = 30;

    private final QuizRepository quizRepository;
    private final SubmissionRepository submissionRepository;
    private final AppUserRepository appUserRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final AnswerRepository answerRepository;

    public StudentQuizService(QuizRepository quizRepository,
            SubmissionRepository submissionRepository,
            AppUserRepository appUserRepository,
            QuestionOptionRepository questionOptionRepository,
            AnswerRepository answerRepository) {
        this.quizRepository = quizRepository;
        this.submissionRepository = submissionRepository;
        this.appUserRepository = appUserRepository;
        this.questionOptionRepository = questionOptionRepository;
        this.answerRepository = answerRepository;
    }

    @Transactional(readOnly = true)
    public List<StudentQuizSummaryResponse> getAllQuizzes() {
        return quizRepository.findAll().stream()
                .map(quiz -> new StudentQuizSummaryResponse(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getDescription(),
                resolveTimeLimit(quiz)))
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentQuizDetailsResponse getQuizDetails(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Quiz not found"));

        List<StudentQuizQuestionResponse> questions = quiz.getQuestions().stream()
                .map(question -> new StudentQuizQuestionResponse(
                question.getId(),
                question.getText(),
                question.getOptions().stream()
                        .map(option -> new StudentQuizOptionResponse(option.getId(), option.getText()))
                        .toList()))
                .toList();

        return new StudentQuizDetailsResponse(quiz.getId(), quiz.getTitle(), questions);
    }

    @Transactional
    public StartQuizResponse startQuiz(Long quizId, String userEmail) {
        AppUser user = getUser(userEmail);
        Quiz quiz = getQuiz(quizId);

        submissionRepository.findByUserIdAndQuizId(user.getId(), quizId)
                .ifPresent(existing -> {
                    if (existing.getStatus() == SubmissionStatus.SUBMITTED) {
                        throw new ApiException(HttpStatus.CONFLICT, "Quiz already submitted by this user");
                    }
                    throw new ApiException(HttpStatus.CONFLICT, "Quiz is already in progress for this user");
                });

        Submission submission = new Submission();
        submission.setUser(user);
        submission.setQuiz(quiz);
        submission.setScore(0);
        submission.setStatus(SubmissionStatus.IN_PROGRESS);
        submission.setStartTime(LocalDateTime.now());

        Submission saved = submissionRepository.save(submission);
        return new StartQuizResponse(saved.getId(), user.getId(), quiz.getId(), saved.getStartTime(), saved.getStatus().name());
    }

    @Transactional
    public StudentSubmitQuizResponse submitQuiz(Long quizId, String userEmail, StudentSubmitQuizRequest request) {
        AppUser user = getUser(userEmail);
        Quiz quiz = getQuiz(quizId);

        Submission submission = submissionRepository.findByUserIdAndQuizId(user.getId(), quizId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Quiz has not been started"));

        if (submission.getStatus() == SubmissionStatus.SUBMITTED) {
            throw new ApiException(HttpStatus.CONFLICT, "Quiz already submitted by this user");
        }

        LocalDateTime deadline = submission.getStartTime().plusMinutes(resolveTimeLimit(quiz));
        if (LocalDateTime.now().isAfter(deadline)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Time limit exceeded for this quiz");
        }

        Set<Long> seenQuestionIds = new HashSet<>();
        int score = 0;

        answerRepository.deleteBySubmissionId(submission.getId());

        for (StudentAnswerRequest answerRequest : request.answers()) {
            if (!seenQuestionIds.add(answerRequest.questionId())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Duplicate answer for question: " + answerRequest.questionId());
            }

            QuestionOption selectedOption = questionOptionRepository
                    .findByIdAndQuestionId(answerRequest.selectedOptionId(), answerRequest.questionId())
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                    "Invalid option for question: " + answerRequest.questionId()));

            Long selectedQuizId = selectedOption.getQuestion().getQuiz().getId();
            if (!quizId.equals(selectedQuizId)) {
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "Question does not belong to this quiz: " + answerRequest.questionId());
            }

            Answer answer = new Answer();
            answer.setSubmission(submission);
            answer.setQuestion(selectedOption.getQuestion());
            answer.setSelectedOption(selectedOption);
            answerRepository.save(answer);

            if (selectedOption.isCorrect()) {
                score++;
            }
        }

        submission.setScore(score);
        submission.setStatus(SubmissionStatus.SUBMITTED);
        submission.setSubmittedAt(LocalDateTime.now());

        Submission saved = submissionRepository.save(submission);
        return new StudentSubmitQuizResponse(saved.getId(), saved.getScore(), saved.getStatus().name(), saved.getSubmittedAt());
    }

    @Transactional(readOnly = true)
    public StudentResultResponse getResult(Long submissionId, String userEmail) {
        AppUser user = getUser(userEmail);

        Submission submission = submissionRepository.findByIdAndUserId(submissionId, user.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Submission not found"));

        int totalQuestions = submission.getQuiz().getQuestions().size();
        return new StudentResultResponse(submission.getScore(), totalQuestions, submission.getSubmittedAt(), submission.getStatus().name());
    }

    private AppUser getUser(String userEmail) {
        return appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private Quiz getQuiz(Long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Quiz not found"));
    }

    private int resolveTimeLimit(Quiz quiz) {
        Integer timeLimit = quiz.getTimeLimit();
        return timeLimit == null ? DEFAULT_TIME_LIMIT_MINUTES : timeLimit;
    }
}
