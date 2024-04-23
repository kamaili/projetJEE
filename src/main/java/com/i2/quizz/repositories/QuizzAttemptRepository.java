package com.i2.quizz.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i2.quizz.entities.Quizz;
import com.i2.quizz.entities.QuizzAttempt;

public interface QuizzAttemptRepository extends JpaRepository<QuizzAttempt, Long>{

    Optional<QuizzAttempt> findByEtudiantIdAndQuizzId(Long idEtudiant, Long idQuizz);

    List<QuizzAttempt> findByQuizz(Quizz quizz);

}
