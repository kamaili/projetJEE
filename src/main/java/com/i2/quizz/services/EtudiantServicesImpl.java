package com.i2.quizz.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i2.quizz.entities.Answer;
import com.i2.quizz.entities.Choice;
import com.i2.quizz.entities.Etudiant;
import com.i2.quizz.entities.Quizz;
import com.i2.quizz.entities.QuizzAttempt;
import com.i2.quizz.repositories.EtudiantRepository;
import com.i2.quizz.repositories.QuizzAttemptRepository;
import com.i2.quizz.repositories.QuizzRepository;


@Service
public class EtudiantServicesImpl implements EtudiantServices{

    @Autowired
    private EtudiantRepository etRep;
    @Autowired
    private QuizzRepository quizzRep;
    @Autowired
    private QuizzAttemptRepository quizzAttemptRep;
	
	
	public Etudiant SaveEtudiant(Etudiant etudiant) {
		if(etRep.existsByUsername(etudiant.getUsername())){
			throw new RuntimeException("Username already exists !!");
		}
		return etRep.save(etudiant);
	}
	public QuizzAttempt rejoindreQuizz(Etudiant etudiant, Quizz quizz) {
		// vérifier d'abord que cet etudiant n'est pas déjà rejoint ce quizz
		QuizzAttempt newTakenQuizz = new QuizzAttempt();
		newTakenQuizz.setQuizz(quizz);
		newTakenQuizz.setEtudiant(etudiant);
		QuizzAttempt savedQuizzAttempt = quizzAttemptRep.save(newTakenQuizz);
		etudiant.getTakenQuizzes().add(savedQuizzAttempt);
		etRep.save(etudiant);
		return savedQuizzAttempt;
	}


	public void QuitterQuiz(Etudiant etudiant,Quizz quizz) {
		QuizzAttempt quizzAttempt = quizzAttemptRep.findByEtudiantIdAndQuizzId(etudiant.getId(), quizz.getId()).orElseThrow(() -> new RuntimeException("this student not taked this quizz")); 
		etudiant.getTakenQuizzes().remove(quizzAttempt);
		etRep.save(etudiant);
		quizzAttemptRep.delete(quizzAttempt);
	}

	public Etudiant modifyEtudiant(Etudiant etudiant) {
		if(etRep.existsByUsername(etudiant.getUsername())){
			throw new RuntimeException("The new username already exists !!");
		}
		return etRep.save(etudiant);
	}

	public int consulterScore(Etudiant etudiant, Quizz quizz) {
		QuizzAttempt quizzAttempt = quizzAttemptRep.findByEtudiantIdAndQuizzId(etudiant.getId(), quizz.getId()).orElseThrow(() -> new RuntimeException("this student not taked this quizz"));
        List<Answer> answers = quizzAttempt.getAnswers();
        int totalCorrect=0;
        for (Answer answer:answers){
            List<Choice> choices = answer.getSelectedChoices();
            for (Choice choice:choices){
                if(! choice.getIsCorrect()){
                    totalCorrect--; // soustraire 1 ici et l'ajouter après ==> 0 
                    break;
                }
            }
            totalCorrect++; // toujours ajouter 1 (si la réponse est fausse ce "1" est déja soustrait ci-dissus) 
        }
        return totalCorrect;
	}
}
