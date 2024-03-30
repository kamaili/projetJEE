package com.quizz.i2.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.i2.entities.Question;


public interface QuestionRepository extends JpaRepository<Question, Long>{
    Optional<Question> findByIdAndQuizzId(Long questionId, Long quizzId);
    
}
