package com.quizz.i2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.i2.entities.Quizz;
import java.util.Optional;


public interface QuizzRepository extends JpaRepository<Quizz, Long>{

    
	//fonction prédifinie
	Optional<Quizz> findByToken(String token);
	

}
