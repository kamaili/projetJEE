package com.i2.quizz.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i2.quizz.entities.Question;


public interface QuestionRepository extends JpaRepository<Question, Long>{
    Optional<Question> findByIdAndQuizzId(Long questionId, Long quizzId);
    
}
