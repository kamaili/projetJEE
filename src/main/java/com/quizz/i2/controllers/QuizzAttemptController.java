package com.quizz.i2.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizz.i2.entities.Answer;
import com.quizz.i2.entities.Choice;
import com.quizz.i2.entities.Question;
import com.quizz.i2.entities.Quizz;
import com.quizz.i2.entities.QuizzAttempt;
import com.quizz.i2.repositories.ChoiceRepository;
import com.quizz.i2.repositories.QuestionRepository;
import com.quizz.i2.repositories.QuizzAttemptRepository;
import com.quizz.i2.services.QuizzAttemptServices;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/quizzattempts")
public class QuizzAttemptController {
    @Autowired
    QuizzAttemptServices quizzAttemptServices;
    @Autowired
    QuizzAttemptRepository quizzAttemptRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ChoiceRepository choiceRepository;

    @PostMapping("/{quizzAttemptId}/setanswer/{questionId}")
    public ResponseEntity<?> setAnswer(@PathVariable Long quizzAttemptId,@PathVariable Long questionId, @RequestBody List<Long> choicesIDs) {
        Optional<QuizzAttempt> quizzAttempt = quizzAttemptRepository.findById(quizzAttemptId);
        Optional<Question> question = questionRepository.findById(questionId);
        if (! quizzAttempt.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("QuizzAttempt Not Found");
        if (! question.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question Not Found");

        Quizz quizz = quizzAttempt.get().getQuizz();
        if (! quizz.getQuestions().contains(question.get()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question Not Found In This Quizz");
        if (choicesIDs == null || choicesIDs.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No choices found");
        
        List<Choice> choices = new ArrayList<Choice>();

        // vérifier que les choices sont existes et liées au question
        for (Long choiceID : choicesIDs) {
            Optional<Choice> choice = choiceRepository.findById(choiceID);
            if ( ! choice.isPresent() || ! question.get().getChoices().contains(choice.get()))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Choices invalides");
            choices.add(choice.get());
        }
    
        Answer answer = quizzAttemptServices.setAnswer(quizzAttempt.get(), question.get(), choices);
        
        return ResponseEntity.ok(answer);
    }
    
}
