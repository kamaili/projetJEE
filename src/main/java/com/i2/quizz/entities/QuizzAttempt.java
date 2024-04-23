package com.i2.quizz.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.i2.quizz.DTO.QuizzAttemptDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "quizz_attempts")
public class QuizzAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int questionAnsweredCount;
    private int score;
    private boolean isCheated;
    
    @ManyToOne
    private Quizz quizz;
    @ManyToOne
    private Etudiant etudiant;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "quizzAttempt")
    List<Answer> answers;
    public QuizzAttemptDto toDto() {
        QuizzAttemptDto dto = new QuizzAttemptDto();
        dto.setId(id);
        dto.setEtudiantId(etudiant.getId());
        dto.setQuizzId(quizz.getId());
        dto.setScore(score);
        dto.setQuestionAnsweredCount(questionAnsweredCount);
        dto.setCheated(isCheated);
        dto.setAnswers(new ArrayList<>());
        if(answers != null)
            for(Answer answer:answers)
                dto.getAnswers().add(answer.toDto());
        return dto;
    }
}