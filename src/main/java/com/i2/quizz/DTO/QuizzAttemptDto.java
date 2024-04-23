package com.i2.quizz.DTO;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizzAttemptDto {

    private Long id;
    private int questionAnsweredCount;
    private int score;
    private boolean isCheated;
    
    
    private Long quizzId;
    private Long etudiantId;
    List<AnswerDto> answers;
}