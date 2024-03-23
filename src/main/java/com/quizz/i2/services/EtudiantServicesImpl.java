package com.quizz.i2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.quizz.i2.entities.Answer;
import com.quizz.i2.entities.Choice;
import com.quizz.i2.entities.Etudiant;
import com.quizz.i2.entities.Quizz;
import com.quizz.i2.entities.QuizzAttempt;
import com.quizz.i2.repositories.EtudiantRepository;
import com.quizz.i2.repositories.ProfesseurRepository;
import com.quizz.i2.repositories.QuizzAttemptRepository;
import com.quizz.i2.repositories.QuizzRepository;

import net.bytebuddy.asm.Advice.Exit;

public class EtudiantServicesImpl implements EtudiantServices{

    @Autowired
    private EtudiantRepository etRep;
    @Autowired
    private QuizzRepository quizzRep;
    @Autowired
    private QuizzAttemptRepository quizzAttemptRep;
	
	
	public void SaveEtudiant(Etudiant etudiant) {
		etRep.save(etudiant);
		System.out.println("Etudiant ajouter avec success !! ");
		
	}
	public void rejoindreQuizz(Etudiant etudiant, String token) {
		Quizz quizz= quizzRep.findByToken(token).orElseThrow(() -> new RuntimeException("Quizz not found"));
		QuizzAttempt newTakenQuizz = new QuizzAttempt();
		newTakenQuizz.setQuizz(quizz);
		etudiant.getTakenQuizzes().add(newTakenQuizz);
		etRep.save(etudiant);
		System.out.println("QuizzAttempt added successfully !! ");
	}


	public void QuitterQuiz(Etudiant etudiant,QuizzAttempt quizzAttempt) {
		
		etudiant.getTakenQuizzes().remove(quizzAttempt);
		etRep.save(etudiant);
		System.out.println("QuizzAttempt removed successfully !! ");
	}

	public void modifyEtudiant(Etudiant etudiant) {
		etRep.save(etudiant);
		System.out.println("Etudiant modifié avec success !! ");
	}

	public int consulterScore(Etudiant etudiant, Quizz quizz) {
		QuizzAttempt quizzAttempt = quizzAttemptRep.findByEtudiantIdAndQuizzId(etudiant.getId(), quizz.getId());
        List<Answer> answers = quizzAttempt.getAnswers();
        int totalCorrect=0;
        for (Answer answer:answers){
            List<Choice> choices = answer.getSelectedChoices();
            for (Choice choice:choices){
                if(! choice.isCorrect()){
                    totalCorrect--; // soustraire 1 ici et l'ajouter après ==> 0 
                    break;
                }
            }
            totalCorrect++; // toujours ajouter 1 (si la réponse est fausse ce "1" est déja soustrait ci-dissus) 
        }
        return totalCorrect;
	}
}
