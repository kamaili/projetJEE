package com.quizz.i2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quizz.i2.entities.Choice;
import com.quizz.i2.entities.Question;
import com.quizz.i2.entities.Quizz;
import com.quizz.i2.repositories.QuestionRepository;
import com.quizz.i2.repositories.QuizzRepository;

@Service
public class QuizzServicesImpl implements QuizzServices{

    @Autowired
    QuizzRepository quizzRep;
    @Autowired
    QuestionRepository questionRep;
        

    @Override
    public void commencerQuizz(Quizz quizz) {
        quizz.setStarted(true);
        quizzRep.save(quizz);
    }

    @Override
    public void arreterQuizz(Quizz quizz) {
        quizz.setStarted(false);
        quizzRep.save(quizz);
    }

    @Override
    public void ajouterQuestion(Quizz quizz, Question question, List<Choice> choices) {
        question.setQuizz(quizz);
        for (Choice choice : choices) {
            choice.setQuestion(question);
        }
        question.setChoices(choices);
        questionRep.save(question);
        quizz.getQuestions().add(question);
        quizzRep.save(quizz);
    }

    @Override
    public void supprimerQuestion(Quizz quizz, Question question) {
        quizz.getQuestions().remove(question);
        quizzRep.save(quizz);
        questionRep.delete(question);
    }
}
