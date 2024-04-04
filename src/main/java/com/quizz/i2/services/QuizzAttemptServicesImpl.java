package com.quizz.i2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quizz.i2.entities.Answer;
import com.quizz.i2.entities.Choice;
import com.quizz.i2.entities.Question;
import com.quizz.i2.entities.QuizzAttempt;
import com.quizz.i2.repositories.AnswerRepository;
import com.quizz.i2.repositories.QuizzAttemptRepository;

@Service
public class QuizzAttemptServicesImpl implements QuizzAttemptServices{

    @Autowired
    private AnswerRepository answerRep;
    @Autowired
    QuizzAttemptRepository quizzAttemptRep;

    public Answer setAnswer(QuizzAttempt quizzAttempt, Question question, List<Choice> choices) {
       Answer answer = answerRep.findByQuestionIdAndQuizzAttemptId(quizzAttempt.getId(),question.getId());
       if(answer == null){
        QuizzAttempt qAttempt = quizzAttemptRep.findById(1L).orElseThrow(() -> new RuntimeException("QuizzAttempt not found"));
        answer = new Answer(null, choices, question, qAttempt);
       }
       answer.setSelectedChoices(choices);
       answerRep.save(answer);
       return answer;
    }

    


}
