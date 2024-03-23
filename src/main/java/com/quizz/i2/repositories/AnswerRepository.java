package com.quizz.i2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.i2.entities.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long>{
    Answer findByQuestionIdAndQuizzAttemptId(Long quizzAttemptId, Long questionId);
}
