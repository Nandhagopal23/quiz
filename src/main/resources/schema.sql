CREATE TABLE IF NOT EXISTS quizzes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    time_limit INT DEFAULT 30,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    quiz_id BIGINT NOT NULL,
    text TEXT NOT NULL,
    CONSTRAINT fk_question_quiz
        FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS question_options (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT NOT NULL,
    text VARCHAR(255) NOT NULL,
    is_correct BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_option_question
        FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
    CONSTRAINT uk_option_id_question UNIQUE (id, question_id)
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL DEFAULT 'ROLE_USER'
);

CREATE TABLE IF NOT EXISTS submissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    quiz_id BIGINT NOT NULL,
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    score INT DEFAULT 0,
    submitted_at TIMESTAMP NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',
    CONSTRAINT uk_submission_user_quiz UNIQUE (user_id, quiz_id),
    CONSTRAINT fk_submission_user
        FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_submission_quiz
        FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

CREATE TABLE IF NOT EXISTS answers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    submission_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    selected_option_id BIGINT NOT NULL,
    CONSTRAINT uk_answer_submission_question UNIQUE (submission_id, question_id),
    CONSTRAINT fk_answer_submission
        FOREIGN KEY (submission_id) REFERENCES submissions(id) ON DELETE CASCADE,
    CONSTRAINT fk_answer_question
        FOREIGN KEY (question_id) REFERENCES questions(id),
    CONSTRAINT fk_answer_selected_option
        FOREIGN KEY (selected_option_id) REFERENCES question_options(id),
    CONSTRAINT fk_answer_option_for_question
        FOREIGN KEY (selected_option_id, question_id)
            REFERENCES question_options(id, question_id)
);
