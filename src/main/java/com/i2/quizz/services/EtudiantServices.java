package com.i2.quizz.services;


import com.i2.quizz.entities.Etudiant;
import com.i2.quizz.entities.Quizz;
import com.i2.quizz.entities.QuizzAttempt;


public interface EtudiantServices {

	public Etudiant SaveEtudiant(Etudiant etudiant);
	public int consulterScore(Etudiant etudiant, Quizz quizz);
	public QuizzAttempt rejoindreQuizz(Etudiant etudiant,Quizz quizz);
	public void QuitterQuiz(Etudiant etudiant,Quizz quizz);
	public Etudiant modifyEtudiant(Etudiant etudiant);
}
