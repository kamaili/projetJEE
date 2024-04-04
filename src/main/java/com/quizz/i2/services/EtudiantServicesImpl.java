package com.quizz.i2.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quizz.i2.entities.Answer;
import com.quizz.i2.entities.Choice;
import com.quizz.i2.entities.Etudiant;
import com.quizz.i2.entities.Quizz;
import com.quizz.i2.entities.QuizzAttempt;
import com.quizz.i2.repositories.EtudiantRepository;
import com.quizz.i2.repositories.QuizzAttemptRepository;
import com.quizz.i2.repositories.QuizzRepository;


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
	public QuizzAttempt rejoindreQuizz(Etudiant etudiant, String token) {
		Quizz quizz= quizzRep.findByToken(token).orElseThrow(() -> new RuntimeException("Quizz not found"));
		// vérifier d'abord que cet etudiant n'est pas déjà rejoint ce quizz
		Optional<QuizzAttempt> quizzAttempt = quizzAttemptRep.findByEtudiantIdAndQuizzId(etudiant.getId(), quizz.getId());
		if(quizzAttempt.isPresent())
			throw new RuntimeException("Quizz already joined by this student");

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
		
		if(! etudiant.getTakenQuizzes().contains(quizzAttempt))
			throw new RuntimeException("quizz not joined before");
		etudiant.getTakenQuizzes().remove(quizzAttempt);
		quizzAttemptRep.delete(quizzAttempt);
		etRep.save(etudiant);
		System.out.println("QuizzAttempt removed successfully !! \n"+etudiant.getTakenQuizzes().size());
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
