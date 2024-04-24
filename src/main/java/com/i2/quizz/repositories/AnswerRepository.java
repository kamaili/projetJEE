package com.i2.quizz.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.i2.quizz.entities.Answer;
import com.i2.quizz.entities.Question;

import jakarta.transaction.Transactional;

public interface AnswerRepository extends JpaRepository<Answer, Long>{
    Answer findByQuestionIdAndQuizzAttemptId(Long quizzAttemptId, Long questionId);
    Optional<Answer> findByQuestion(Question question);
    
    @Modifying  // Optional but recommended for delete queries
    @Query("DELETE FROM Answer a WHERE a.id = :answerId")
    @Transactional
    void forcerDelete(@Param("answerId") Long answerId);
}
