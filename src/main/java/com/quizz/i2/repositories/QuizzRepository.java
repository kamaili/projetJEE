package com.quizz.i2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.i2.entities.Quizz;

public interface QuizzRepository extends JpaRepository<Quizz, Long>{

}
