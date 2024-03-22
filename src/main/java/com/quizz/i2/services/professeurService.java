package com.quizz.i2.services;

import com.quizz.i2.entities.Etudiant;
import com.quizz.i2.entities.Professeur;
import com.quizz.i2.entities.Quizz;

public interface professeurService {
    public Professeur enregistrerProfesseur(Professeur prof);
    public Professeur modifierProfesseur(Professeur prof);
    public Quizz createQuizz(Professeur prof, Quizz quizz);
    public int consulterScore(Professeur prof, Quizz quizz, Etudiant etudiant);
}
