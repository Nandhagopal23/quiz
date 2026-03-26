package com.example.quiz.service;

import com.example.quiz.api.dto.submission.QuestionAnswerRequest;
import com.example.quiz.api.dto.submission.SubmissionResponse;
import com.example.quiz.api.dto.submission.SubmitQuizRequest;
import com.example.quiz.exception.ApiException;
import com.example.quiz.model.Answer;
import com.example.quiz.model.AppUser;
import com.example.quiz.model.Question;
import com.example.quiz.model.QuestionOption;
import com.example.quiz.model.Quiz;
import com.example.quiz.model.Submission;
import com.example.quiz.repository.AnswerRepository;
import com.example.quiz.repository.AppUserRepository;
import com.example.quiz.repository.QuestionOptionRepository;
import com.example.quiz.repository.QuestionRepository;
import com.example.quiz.repository.QuizRepository;
import com.example.quiz.repository.SubmissionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AppUserRepository appUserRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final AnswerRepository answerRepository;

    public SubmissionService(SubmissionRepository submissionRepository,
                             AppUserRepository appUserRepository,
                             QuizRepository quizRepository,
                             QuestionRepository questionRepository,
                             QuestionOptionRepository questionOptionRepository,
                             AnswerRepository answerRepository) {
        this.submissionRepository = submissionRepository;
        this.appUserRepository = appUserRepository;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.questionOptionRepository = questionOptionRepository;
        this.answerRepository = answerRepository;
    }

    @Transactional
    public SubmissionResponse submitQuiz(Long quizId, String userEmail, SubmitQuizRequest request) {
        AppUser user = appUserRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Quiz not found"));

        if (submissionRepository.existsByUserIdAndQuizId(user.getId(), quizId)) {
            throw new ApiException(HttpStatus.CONFLICT, "User has already submitted this quiz");
        }

        Submission submission = new Submission();
        submission.setQuiz(quiz);
        submission.setUser(user);
        submission.setScore(0);
        Submission savedSubmission = submissionRepository.save(submission);

        int score = 0;
        for (QuestionAnswerRequest answerRequest : request.answers()) {
            Question question = questionRepository.findById(answerRequest.questionId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Invalid question id: " + answerRequest.questionId()));

            if (!question.getQuiz().getId().equals(quizId)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Question does not belong to this quiz: " + answerRequest.questionId());
            }

            QuestionOption selectedOption = questionOptionRepository
                .findByIdAndQuestionId(answerRequest.selectedOptionId(), answerRequest.questionId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Selected option is invalid for question: " + answerRequest.questionId()));

            Answer answer = new Answer();
            answer.setSubmission(savedSubmission);
            answer.setQuestion(question);
            answer.setSelectedOption(selectedOption);
            answerRepository.save(answer);

            if (selectedOption.isCorrect()) {
                score++;
            }
        }

        savedSubmission.setScore(score);
        Submission finalSubmission = submissionRepository.save(savedSubmission);

        return new SubmissionResponse(
            finalSubmission.getId(),
            finalSubmission.getQuiz().getId(),
            finalSubmission.getUser().getId(),
            finalSubmission.getScore(),
            finalSubmission.getSubmittedAt()
        );
    }
}
