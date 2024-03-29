package com.quizz.i2.services;

import com.quizz.i2.entities.Professeur;
import com.quizz.i2.entities.Quizz;


public interface ProfesseurServices {
    public Professeur enregistrerProfesseur(Professeur prof);
    public Professeur modifierProfesseur(Professeur prof);
    public void createQuizz(Professeur prof, Quizz quizz);
    public void deleteQuizz(Professeur prof, Quizz quizz);
    /* method removed
    public int consulterScore(Professeur prof, Quizz quizz, Etudiant etudiant);
    */
}
