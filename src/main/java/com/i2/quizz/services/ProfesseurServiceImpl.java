package com.i2.quizz.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i2.quizz.entities.Professeur;
import com.i2.quizz.repositories.ProfesseurRepository;

@Service
public class ProfesseurServiceImpl implements ProfesseurServices{

    @Autowired
    ProfesseurRepository profRep;
    
    
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
    
    // must check if quizz created by this prof
    
    /* Removed
    public int consulterScore(Professeur prof, Quizz quizz, Etudiant etudiant){
        // la meme méthode de l'interface EtudiantServices
        return etudServices.consulterScore(etudiant,quizz);
    }
    */
    

}
