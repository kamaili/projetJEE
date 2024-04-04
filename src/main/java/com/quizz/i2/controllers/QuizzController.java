package com.quizz.i2.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quizz.i2.entities.Question;
import com.quizz.i2.entities.Quizz;
import com.quizz.i2.repositories.QuestionRepository;
import com.quizz.i2.repositories.QuizzRepository;
import com.quizz.i2.services.QuizzServices;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/quizzes")
public class QuizzController {
    @Autowired
    QuizzServices quizzServices;
    @Autowired
    QuizzRepository quizzRepository;
    @Autowired
    QuestionRepository questionRepository;

    @PatchMapping("/{quizzId}/status")
    public ResponseEntity<?> setQuizzStatus(@PathVariable Long quizzId, @RequestParam boolean started) {
        Optional<Quizz> quizz = quizzRepository.findById(quizzId);
        if(! quizz.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("quizz not found");

            if(started)
                quizzServices.commencerQuizz(quizz.get());
            else 
                quizzServices.arreterQuizz(quizz.get());
        return ResponseEntity.ok("Quizz staus changed to: "+started);
    }

    @PostMapping("{quizzId}/addquestion")
    public ResponseEntity<?> creerQuestion(@PathVariable Long quizzId, @RequestBody Question question ) {
        Optional<Quizz> quizz = quizzRepository.findById(quizzId);
        if(! quizz.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("quizz not found");
        if(question.getChoices() == null || question.getChoices().isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No choices found");

        quizzServices.ajouterQuestion(quizz.get(), question, question.getChoices());
        
        return ResponseEntity.ok("Question added to the specific quizz");
    }
    
    @DeleteMapping("{quizzId}/deletequestion/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long quizzId, @PathVariable Long questionId){
        Optional<Quizz> quizz = quizzRepository.findById(quizzId);
        Optional<Question> question = questionRepository.findByIdAndQuizzId(questionId,quizzId);
        if(! quizz.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("quizz not found");
        if(! question.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("question not present on this quizz");
        quizzServices.supprimerQuestion(quizz.get(), question.get());        
        return ResponseEntity.ok("Question deleted from the specific quizz");
    }
}

// à completer