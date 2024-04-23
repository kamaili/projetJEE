package com.i2.quizz.DTO;
import com.i2.quizz.entities.Choice;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChoiceDto {
    private Long id;

    private String text;
    private Boolean isCorrect;
    private Long questionId;

    @Override
    public String toString(){return text;}


    
}
