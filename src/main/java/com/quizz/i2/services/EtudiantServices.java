package com.quizz.i2.services;


import com.quizz.i2.entities.Etudiant;
import com.quizz.i2.entities.Quizz;
import com.quizz.i2.entities.QuizzAttempt;


public interface EtudiantServices {

	public Etudiant SaveEtudiant(Etudiant etudiant);
	public int consulterScore(Etudiant etudiant, Quizz quizz);
	public QuizzAttempt rejoindreQuizz(Etudiant etudiant,String token);
	public void QuitterQuiz(Etudiant etudiant,Quizz quizz);
	public Etudiant modifyEtudiant(Etudiant etudiant);
}
