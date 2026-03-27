package com.example.quiz.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz.api.dto.quiz.CreateOptionRequest;
import com.example.quiz.api.dto.quiz.CreateQuestionRequest;
import com.example.quiz.api.dto.teacher.StudentScoreResponse;
import com.example.quiz.api.dto.teacher.TeacherCreateQuizRequest;
import com.example.quiz.api.dto.teacher.TeacherQuizSummaryResponse;
import com.example.quiz.api.dto.teacher.TeacherUpdateQuizRequest;
import com.example.quiz.exception.ApiException;
import com.example.quiz.model.AppUser;
import com.example.quiz.model.Question;
import com.example.quiz.model.QuestionOption;
import com.example.quiz.model.Quiz;
import com.example.quiz.repository.QuizRepository;
import com.example.quiz.repository.SubmissionRepository;
import com.example.quiz.repository.AppUserRepository;

@Service
public class TeacherQuizService {

    private final QuizRepository quizRepository;
    private final SubmissionRepository submissionRepository;
    private final AppUserRepository userRepository;

    public TeacherQuizService(QuizRepository quizRepository,
            SubmissionRepository submissionRepository,
            AppUserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TeacherQuizSummaryResponse createQuiz(TeacherCreateQuizRequest request, String teacherEmail) {
        AppUser teacher = findUserByEmail(teacherEmail);

        Quiz quiz = new Quiz();
        quiz.setTeacher(teacher);
        quiz.setTitle(request.title());
        quiz.setDescription(request.description());
        quiz.setStartTime(request.startTime());
        if (request.timeLimitMinutes() != null) {
            quiz.setTimeLimit(request.timeLimitMinutes());
        }

        addQuestions(quiz, request.questions());
        Quiz saved = quizRepository.save(quiz);
        return toSummary(saved);
    }

    @Transactional(readOnly = true)
    public List<TeacherQuizSummaryResponse> getAllQuizzes() {
        return quizRepository.findAll().stream().map(this::toSummary).toList();
    }

    @Transactional
    public TeacherQuizSummaryResponse updateQuiz(Long quizId, TeacherUpdateQuizRequest request, String teacherEmail) {
        Quiz quiz = findQuizById(quizId);
        validateOwnerAndNotStarted(quiz, teacherEmail);

        if (request.title() != null && !request.title().isBlank()) {
            quiz.setTitle(request.title());
        }
        if (request.description() != null) {
            quiz.setDescription(request.description());
        }
        if (request.timeLimitMinutes() != null) {
            quiz.setTimeLimit(request.timeLimitMinutes());
        }
        if (request.startTime() != null) {
            quiz.setStartTime(request.startTime());
        }
        if (request.questions() != null && !request.questions().isEmpty()) {
            quiz.getQuestions().clear();
            addQuestions(quiz, request.questions());
        }

        return toSummary(quizRepository.save(quiz));
    }

    @Transactional
    public void deleteQuiz(Long quizId, String teacherEmail) {
        Quiz quiz = findQuizById(quizId);
        validateOwnerAndNotStarted(quiz, teacherEmail);
        quizRepository.delete(quiz);
    }

    @Transactional(readOnly = true)
    public List<StudentScoreResponse> getStudentScores(Long quizId) {
        findQuizById(quizId); // validates quiz exists
        return submissionRepository.findByQuizId(quizId).stream()
                .map(sub -> new StudentScoreResponse(
                        sub.getUser().getId(),
                        sub.getUser().getName(),
                        sub.getUser().getEmail(),
                        sub.getScore(),
                        sub.getStatus().name(),
                        sub.getSubmittedAt()
                ))
                .toList();
    }

    // ---- helpers ----

    private void addQuestions(Quiz quiz, List<CreateQuestionRequest> questionRequests) {
        for (CreateQuestionRequest qr : questionRequests) {
            Question question = new Question();
            question.setText(qr.text());
            question.setQuiz(quiz);

            int correctCount = 0;
            for (CreateOptionRequest or : qr.options()) {
                QuestionOption opt = new QuestionOption();
                opt.setText(or.text());
                opt.setCorrect(or.correct());
                opt.setQuestion(question);
                question.getOptions().add(opt);
                if (or.correct()) {
                    correctCount++;
                }
            }
            if (correctCount == 0) {
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "Question \"" + qr.text() + "\" must have at least one correct option");
            }
            quiz.getQuestions().add(question);
        }
    }

    private void validateOwnerAndNotStarted(Quiz quiz, String teacherEmail) {
        if (quiz.getTeacher() == null || !quiz.getTeacher().getEmail().equals(teacherEmail)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You are not the owner of this quiz");
        }
        if (quiz.getStartTime() != null && LocalDateTime.now().isAfter(quiz.getStartTime())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Quiz has already started and cannot be modified");
        }
    }

    private AppUser findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Teacher not found: " + email));
    }

    private Quiz findQuizById(Long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Quiz not found"));
    }

    private TeacherQuizSummaryResponse toSummary(Quiz quiz) {
        String teacherName = quiz.getTeacher() != null ? quiz.getTeacher().getName() : "Unknown";
        return new TeacherQuizSummaryResponse(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getDescription(),
                quiz.getTimeLimit(),
                quiz.getStartTime(),
                quiz.getQuestions().size(),
                teacherName,
                quiz.getCreatedAt()
        );
    }
}
