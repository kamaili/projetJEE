package com.quizz.i2.services;


import java.util.List;

import com.quizz.i2.entities.Choice;
import com.quizz.i2.entities.Question;
import com.quizz.i2.entities.Quizz;


public interface QuizzServices {
    public void commencerQuizz(Quizz quizz);
    public void arreterQuizz(Quizz quizz);
    public void ajouterQuestion(Quizz quizz, Question question, List<Choice> choices);
    public void supprimerQuestion(Quizz quizz, Question question);
    
}
