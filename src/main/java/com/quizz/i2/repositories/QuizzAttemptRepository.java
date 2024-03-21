package com.quizz.i2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.i2.entities.QuizzAttempt;

public interface QuizzAttemptRepository extends JpaRepository<QuizzAttempt, Long>{

}
