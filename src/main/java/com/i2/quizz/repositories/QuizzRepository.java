package com.i2.quizz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i2.quizz.entities.Quizz;
import java.util.Optional;


public interface QuizzRepository extends JpaRepository<Quizz, Long>{

    
	//fonction prédifinie
	Optional<Quizz> findByToken(String token);
	

}
