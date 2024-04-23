package com.i2.quizz.services;


import java.util.List;
import java.util.Map;

import com.i2.quizz.DTO.EtudiantDto;
import com.i2.quizz.DTO.QuestionDto;
import com.i2.quizz.entities.Choice;
import com.i2.quizz.entities.Etudiant;
import com.i2.quizz.entities.Professeur;
import com.i2.quizz.entities.Question;
import com.i2.quizz.entities.Quizz;


public interface QuizzServices {
    public void commencerQuizz(Quizz quizz);
    public void arreterQuizz(Quizz quizz);
    public QuestionDto ajouterQuestion(Quizz quizz, Question question, List<Choice> choices);
    public void modifierQuestion(QuestionDto newQuestion);
    public void supprimerQuestion(Quizz quizz, Question question);
    public Quizz createQuizz(Professeur prof, Map<String,Object> quizzData);
    public void open(Quizz quizz);
    public void deleteQuizz(Professeur prof, Quizz quizz);
    public List<EtudiantDto> joinedStudents(Quizz quizz);
    public List<Map<String,Object>> getProgress(Quizz quizz);
    
}
