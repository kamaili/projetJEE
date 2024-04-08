package com.quizz.i2.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.i2.entities.Etudiant;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long>{
    boolean existsByUsername(String username);
    Optional<Etudiant> findByUsernameAndPassword(String username, String password);

    
}
