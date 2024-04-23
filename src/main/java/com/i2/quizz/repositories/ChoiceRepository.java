package com.i2.quizz.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.i2.quizz.entities.Choice;
import com.i2.quizz.entities.Question;

import jakarta.transaction.Transactional;

public interface ChoiceRepository extends JpaRepository<Choice, Long>{

    List<Choice> findByQuestionAndIsCorrect(Question question, boolean isCorrect);

    @Modifying  // Optional but recommended for delete queries
    @Query("DELETE FROM Choice c WHERE c.id = :choiceId")
    @Transactional
    void deleteChoiceById(@Param("choiceId") Long choiceId);
}
