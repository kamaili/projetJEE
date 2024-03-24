package com.quizz.i2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quizz.i2.entities.Etudiant;
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
			System.out.println("username existes !! ");
			return null;
		}
        return profRep.save(prof);
    }
    public Professeur modifierProfesseur(Professeur prof){
        if(profRep.existsByUsername(prof.getUsername())){
			System.out.println("username existes !! ");
			return null;
		}
        return profRep.save(prof);
    }
    public Quizz createQuizz(Professeur prof, Quizz quizz){
        return quizzRep.save(quizz);
    }
    public int consulterScore(Professeur prof, Quizz quizz, Etudiant etudiant){
        // la meme méthode de l'interface EtudiantServices
        return etudServices.consulterScore(etudiant,quizz);
    }

}
