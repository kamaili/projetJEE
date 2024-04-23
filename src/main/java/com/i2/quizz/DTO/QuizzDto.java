package com.i2.quizz.DTO;
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
public class QuizzDto {

    private Long id;

    private String title;
    private int duration;
    private String token;
    private boolean started;
    private boolean finished;
    private boolean openToJoin;
    private int maxParticipations;

    private Long professeurId;

    private List<QuestionDto> questions;
}
