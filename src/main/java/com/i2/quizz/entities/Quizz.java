package com.i2.quizz.entities;

import java.util.ArrayList;
import java.util.List;

import com.i2.quizz.DTO.QuestionDto;
import com.i2.quizz.DTO.QuizzDto;

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
@Table(name = "quizzes")
public class Quizz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private int duration;
    private String token;
    private boolean started;
    private boolean finished;
    private boolean openToJoin;
    private int maxParticipations;

    @ManyToOne
    private Professeur professeur;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "quizz", fetch = FetchType.EAGER)
    private List<Question> questions;


    public QuizzDto toDto(){
        QuizzDto dto = new QuizzDto(id, title, duration, token, started, finished, openToJoin, maxParticipations, id, null);
        List<QuestionDto> list = new ArrayList<>();
        for(Question question:questions)
            list.add(question.toDto());
        dto.setQuestions(list);
        return dto;
    }
}
