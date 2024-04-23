package com.i2.quizz.entities;

import java.util.ArrayList;
import java.util.List;

import com.i2.quizz.DTO.AnswerDto;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "answers_choices",joinColumns = @JoinColumn(name = "answer_id"),
    inverseJoinColumns = @JoinColumn(name = "choice_id"))
    private List<Choice> selectedChoices;

    @ManyToOne
    private Question question;
    @ManyToOne
    private QuizzAttempt quizzAttempt;
    public AnswerDto toDto() {
        AnswerDto dto = new AnswerDto();
        dto.setId(id);
        if(selectedChoices != null)
            dto.setSelectedChoices(new ArrayList<>());
        for(Choice choice:selectedChoices)
            dto.getSelectedChoices().add(choice.toDto());
        return dto;
    }
}
