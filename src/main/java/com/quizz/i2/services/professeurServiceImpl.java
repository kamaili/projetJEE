package com.quizz.i2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.quizz.i2.entities.Answer;
import com.quizz.i2.entities.Etudiant;
import com.quizz.i2.entities.Professeur;
import com.quizz.i2.entities.Quizz;
import com.quizz.i2.entities.QuizzAttempt;
import com.quizz.i2.repositories.ProfesseurRepository;
import com.quizz.i2.repositories.QuizzAttemptRepository;
import com.quizz.i2.repositories.QuizzRepository;


public class professeurServiceImpl implements professeurService{

    @Autowired
    private ProfesseurRepository profRep;
    @Autowired
    private QuizzRepository quizzRep;
    @Autowired
    private QuizzAttemptRepository quizzAttemptRep;

    public Professeur enregistrerProfesseur(Professeur prof){
        return profRep.save(prof);
    }
    public Professeur modifierProfesseur(Professeur prof){
        return profRep.save(prof);
    }
    public Quizz createQuizz(Professeur prof, Quizz quizz){
        return quizzRep.save(quizz);
    }
    public int consulterScore(Professeur prof, Quizz quizz, Etudiant etudiant){
        QuizzAttempt quizzAttempt = quizzAttemptRep.findByEtudiantIdAndQuizzId(etudiant.getId(), quizz.getId());
        List<Answer> answers = quizzAttempt.getAnswers();
        int totalCorrect=0;
        for (Answer answer:answers){
            if(answer.getSelectedAnswer() == answer.getQuestion().getCorrect_answer())
                totalCorrect++;
        }
        return totalCorrect;
    }

}
