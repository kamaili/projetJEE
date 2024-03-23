package com.quizz.i2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.quizz.i2.entities.Quizz;
import java.util.List;
import java.util.Optional;


public interface QuizzRepository extends JpaRepository<Quizz, Long>{

	//fonction prédifinie
	Quizz findByToken(String token);
	

}
