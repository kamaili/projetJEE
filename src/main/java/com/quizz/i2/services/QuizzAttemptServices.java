package com.quizz.i2.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.quizz.i2.entities.Choice;
import com.quizz.i2.entities.Question;
import com.quizz.i2.entities.QuizzAttempt;


public interface QuizzAttemptServices {
    public void setAnswer(QuizzAttempt quizzAttempt, Question question, List<Choice> choices);
    
}
