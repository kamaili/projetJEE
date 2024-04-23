package com.i2.quizz.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i2.quizz.entities.Etudiant;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long>{
    boolean existsByUsername(String username);
    Optional<Etudiant> findByUsernameAndPassword(String username, String password);

    
}
