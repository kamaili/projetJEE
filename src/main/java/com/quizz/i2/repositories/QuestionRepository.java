package com.quizz.i2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.i2.entities.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>{
    
}