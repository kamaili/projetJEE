package com.quizz.i2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.i2.entities.Etudiant;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long>{
    
}
