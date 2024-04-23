package com.i2.quizz.services;

import java.util.List;
import java.util.Map;

import com.i2.quizz.entities.Professeur;
import com.i2.quizz.entities.Quizz;


public interface ProfesseurServices {
    public Professeur enregistrerProfesseur(Professeur prof);
    public Professeur modifierProfesseur(Professeur prof);
}
