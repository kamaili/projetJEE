package com.quizz.i2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.i2.entities.Professeur;

public interface ProfesseurRepository extends JpaRepository<Professeur, Long>{
    
}
