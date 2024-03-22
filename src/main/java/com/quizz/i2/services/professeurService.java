package com.quizz.i2.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import com.quizz.i2.entities.Professeur;
import com.quizz.i2.entities.Quizz;
import com.quizz.i2.repositories.ProfesseurRepository;
import com.quizz.i2.repositories.QuizzRepository;


public class professeurService {

    @Autowired
    private ProfesseurRepository profRep;
    @Autowired
    private
    QuizzRepository quizzRep;

    public Professeur enregistrerProfesseur(Professeur prof){
        return profRep.save(prof);
    }
    public Professeur modifierProfesseur(Professeur prof){
        return profRep.save(prof);
    }
    public Quizz createQuizz(Professeur prof, Quizz quizz){
        return quizzRep.save(quizz);
    }

}
