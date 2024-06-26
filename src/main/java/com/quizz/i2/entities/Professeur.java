package com.quizz.i2.entities;

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
@DiscriminatorValue(value = "PROF")
public class Professeur extends Utilisateur{

    private String departement;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "professeur")
    private List<Quizz> createdQuizzes;
}
