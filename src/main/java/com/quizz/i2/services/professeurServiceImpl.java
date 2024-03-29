package com.quizz.i2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quizz.i2.entities.Professeur;
import com.quizz.i2.entities.Quizz;
import com.quizz.i2.repositories.ProfesseurRepository;
import com.quizz.i2.repositories.QuizzRepository;

@Service
public class ProfesseurServiceImpl implements ProfesseurServices{

    @Autowired
    private ProfesseurRepository profRep;
    @Autowired
    private QuizzRepository quizzRep;

    @Autowired
    EtudiantServices etudServices;

    public Professeur enregistrerProfesseur(Professeur prof){
        if(profRep.existsByUsername(prof.getUsername())){
			throw new RuntimeException("Username already exists !!");
		}
		return profRep.save(prof);
    }
    public Professeur modifierProfesseur(Professeur prof){
        if(profRep.existsByUsername(prof.getUsername())){
			throw new RuntimeException("The new username already exists !!");
		}
		return profRep.save(prof);
    }
    public void createQuizz(Professeur prof, Quizz quizz){
        quizz.setProfesseur(prof);
        quizzRep.save(quizz);
        prof.getCreatedQuizzes().add(quizz);
        profRep.save(prof);
    }
    // must check if quizz created by this prof
    public void deleteQuizz(Professeur prof, Quizz quizz){
        if(! prof.getCreatedQuizzes().contains(quizz))
            throw new RuntimeException("this prof have not created this quizz before");
        prof.getCreatedQuizzes().remove(quizz);
        quizzRep.delete(quizz);
        profRep.save(prof);
    }
    /* Removed
    public int consulterScore(Professeur prof, Quizz quizz, Etudiant etudiant){
        // la meme méthode de l'interface EtudiantServices
        return etudServices.consulterScore(etudiant,quizz);
    }
    */

}
