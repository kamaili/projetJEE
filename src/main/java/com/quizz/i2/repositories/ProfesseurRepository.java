package com.quizz.i2.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.i2.entities.Professeur;

public interface ProfesseurRepository extends JpaRepository<Professeur, Long>{
    boolean existsByUsername(String username);
    Optional<Professeur> findByUsernameAndPassword(String username, String password);
    
}
