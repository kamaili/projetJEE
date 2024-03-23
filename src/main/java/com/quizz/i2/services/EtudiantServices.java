package com.quizz.i2.services;

import com.quizz.i2.entities.Etudiant;
import com.quizz.i2.entities.Quizz;
import com.quizz.i2.entities.QuizzAttempt;

public interface EtudiantServices {

	public void SaveEtudiant(Etudiant e);
	public int consultScore(Etudiant e,Quizz q);
	public void rejoindreQuizz(Etudiant e,String token);
	public void QuitterQuiz();
	public void modifyEtudiant(Etudiant e);
}
