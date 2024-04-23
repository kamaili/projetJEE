package com.i2.quizz.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i2.quizz.entities.Professeur;

public interface ProfesseurRepository extends JpaRepository<Professeur, Long>{
    boolean existsByUsername(String username);
    Optional<Professeur> findByUsernameAndPassword(String username, String password);
    
}
