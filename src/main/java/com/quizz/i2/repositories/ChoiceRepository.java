package com.quizz.i2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.i2.entities.Choice;

public interface ChoiceRepository extends JpaRepository<Choice, Long>{
}
