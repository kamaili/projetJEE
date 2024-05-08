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
		return profRep.save(prof);
    }
    public Professeur modifierProfesseur(Professeur prof){
		return profRep.save(prof);
    }
}
