package com.i2.quizz.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.i2.quizz.DTO.ChoiceDto;
import com.i2.quizz.DTO.QuestionDto;

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
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Choice> choices;
    
    @ManyToOne
    private Quizz quizz;

    @Override
    public String toString(){return text;}
    
    public QuestionDto toDto(){
        QuestionDto dto = new QuestionDto(id, text, null, quizz.getId());
        List<ChoiceDto> list = new ArrayList<>();
        for(Choice choice: choices)
            list.add(choice.toDto());
        dto.setChoices(list);
        return dto;
    }
    
}