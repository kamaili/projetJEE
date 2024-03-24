package com.quizz.i2.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quizz.i2.entities.Question;
import com.quizz.i2.entities.Quizz;
import com.quizz.i2.repositories.QuestionRepository;
import com.quizz.i2.repositories.QuizzRepository;

@Service
public class QuizzServicesImpl implements QuizzServices{

    @Autowired
    QuizzRepository quizzRep;
    @Autowired
        QuestionRepository qr;
        

    @Override
    public void commencerQuizz(Quizz quizz) {
        Quizz q = quizzRep.findById(quizz.getId()).orElseThrow(() -> new RuntimeException("Quizz not found"));
        q.setStarted(true);
        quizzRep.save(q);
    }

    @Override
    public void arreterQuizz(Quizz quizz) {
        Quizz q = quizzRep.findById(quizz.getId()).orElseThrow(() -> new RuntimeException("Quizz not found"));
        q.setStarted(false);
        quizzRep.save(q);
    }

    @Override
    public void ajouterQuestion(Quizz quizz, Question question) {
        Quizz q = quizzRep.findById(quizz.getId()).orElseThrow(() -> new RuntimeException("Quizz not found"));
        question.setQuizz(q);
        qr.save(question);
        //Hibernate.initialize(q.getQuestions()); // Initialize the questions collection
        q.getQuestions().add(question);
        quizzRep.save(quizz);
        System.out.println(q.getQuestions());
    }

    @Override
    public void supprimerQuestion(Quizz quizz, Question question) {
        Quizz q = quizzRep.findById(quizz.getId()).orElseThrow(() -> new RuntimeException("Quizz not found"));
        q.getQuestions().remove(question);
        quizzRep.save(quizz);
    }


}
