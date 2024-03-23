package com.quizz.i2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.quizz.i2.entities.Answer;
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
	
	public void SaveEtudiant(Etudiant e) {
		etRep.save(e);
		System.out.println("Etudiant ajouter avec success !! ");
		
	}


	public int consultScore(Etudiant e, QuizzAttempt q) {
        QuizzAttempt quizzAttempt = quizzAttemptRep.findByEtudiantIdAndQuizzId(etudiant.getId(), quizz.getId());
        List<Answer> answers = quizzAttempt.getAnswers();
        int totalscore=0;
        for (Answer answer:answers){
            if(answer.getSelectedAnswer() == answer.getQuestion().getCorrect_answer())
                totalscore +=answer.getQuestion().getValue();
        }
        return totalCorrect;
    }
		
		
	}


	public void rejoindreQuizz(Etudiant e, String token) {
		Quizz q= quizzRep.findQuizzByToken(token);
		List l;
		l=e.getTakenQuizzes();
		l.add(q);
		e.setTakenQuizzes(l);
	}


	public void QuitterQuiz() {
		
	}

	public void modifyEtudiant(Etudiant e) {
		
		
	}

	
	
}
