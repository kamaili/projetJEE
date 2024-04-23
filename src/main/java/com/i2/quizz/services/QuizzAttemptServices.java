package com.i2.quizz.services;

import java.util.List;
import java.util.Map;

import com.i2.quizz.entities.Answer;
import com.i2.quizz.entities.Choice;
import com.i2.quizz.entities.Question;
import com.i2.quizz.entities.QuizzAttempt;


public interface QuizzAttemptServices {
    public Answer setAnswer(QuizzAttempt quizzAttempt, Question question, List<Choice> choices);
    public Answer updateAnswer(QuizzAttempt quizzAttempt, Question question, List<Choice> choices);
    public void deleteAnswer(QuizzAttempt quizzAttempt, Question question);
    public Map<String,Integer> getScoring(QuizzAttempt quizzAttempt);
    
}
