package com.quizz.i2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.quizz.i2.entities.Answer;
import com.quizz.i2.entities.Choice;
import com.quizz.i2.entities.Question;
import com.quizz.i2.entities.QuizzAttempt;
import com.quizz.i2.repositories.AnswerRepository;
import com.quizz.i2.repositories.QuizzAttemptRepository;

public class QuizzAttemptServicesImpl implements QuizzAttemptServices{

    @Autowired
    private AnswerRepository answerRep;

    public void setAnswer(QuizzAttempt quizzAttempt, Question question, List<Choice> choices) {
       Answer answer = answerRep.findByQuestionIdAndQuizzAttemptId(quizzAttempt.getId(),question.getId());
       answer.setSelectedChoices(choices);
       answerRep.save(answer);
    }

    


}
