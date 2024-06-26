package com.quizz.i2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.i2.entities.Quizz;
import com.quizz.i2.entities.QuizzAttempt;

public interface QuizzAttemptRepository extends JpaRepository<QuizzAttempt, Long>{

    Optional<QuizzAttempt> findByEtudiantIdAndQuizzId(Long idEtudiant, Long idQuizz);

    List<QuizzAttempt> findByQuizz(Quizz quizz);

}
