package com.quizz.i2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.quizz.i2.entities.Quizz;

public interface QuizzRepository extends JpaRepository<Quizz, Long>{
	@Query("select q from Quizz q where q.token = :t")
	Quizz findQuizzByToken(@Param("t")String token);
	

}
