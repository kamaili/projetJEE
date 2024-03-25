package com.quizz.i2.entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class QuizzAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date startedAt;
    private Date finishedAt;
    private boolean isPassed;
    
    @ManyToOne
    private Quizz quizz;
    @ManyToOne
    private Etudiant etudiant;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "quizzAttempt",fetch = FetchType.EAGER)
    List<Answer> answers;
}