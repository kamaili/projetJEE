 package com.quizz.i2.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue(value = "ETUD")
public class Etudiant extends Utilisateur{
    
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "etudiant")
    private List<QuizzAttempt> takenQuizzes = new ArrayList<>();


}
