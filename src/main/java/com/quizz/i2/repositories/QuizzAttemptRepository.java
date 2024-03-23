package com.quizz.i2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.quizz.i2.entities.QuizzAttempt;

public interface QuizzAttemptRepository extends JpaRepository<QuizzAttempt, Long>{

    QuizzAttempt findByEtudiantIdAndQuizzId(Long idEtudiant, Long idQuizz);

}
