package com.i2.quizz.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i2.quizz.DTO.ChoiceDto;
import com.i2.quizz.entities.Answer;
import com.i2.quizz.entities.Choice;
import com.i2.quizz.entities.Question;
import com.i2.quizz.entities.Quizz;
import com.i2.quizz.entities.QuizzAttempt;
import com.i2.quizz.repositories.ChoiceRepository;
import com.i2.quizz.repositories.QuestionRepository;
import com.i2.quizz.repositories.QuizzAttemptRepository;
import com.i2.quizz.services.QuizzAttemptServices;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;



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
    public ResponseEntity<?> setAnswer(@PathVariable Long quizzAttemptId,@PathVariable Long questionId, @RequestBody List<ChoiceDto> choicesDtos) {
        QuizzAttempt quizzAttempt = quizzAttemptRepository.findById(quizzAttemptId).get();
        Question question = questionRepository.findById(questionId).get();
        List<Choice> choices = new ArrayList<>();
        for(ChoiceDto choiceDto : choicesDtos){
            Choice choice = choiceRepository.findById(choiceDto.getId()).get();
            choices.add(choice);
        }
        Answer answer = quizzAttemptServices.setAnswer(quizzAttempt, question, choices);
        
        return ResponseEntity.ok(answer.toDto());
    }
    @PutMapping("/{quizzAttemptId}/updateanswer/{questionId}")
    public ResponseEntity<?> updateAnswer(@PathVariable Long quizzAttemptId,@PathVariable Long questionId, @RequestBody List<ChoiceDto> choicesDtos) {
        QuizzAttempt quizzAttempt = quizzAttemptRepository.findById(quizzAttemptId).get();
        Question question = questionRepository.findById(questionId).get();
        List<Choice> choices = new ArrayList<>();
        for(ChoiceDto choiceDto : choicesDtos){
            Choice choice = choiceRepository.findById(choiceDto.getId()).get();
            choices.add(choice);
        }
        Answer answer = quizzAttemptServices.updateAnswer(quizzAttempt, question, choices);
        
        return ResponseEntity.ok(answer.toDto());
    }
    @DeleteMapping("/{quizzAttemptId}/deleteanswer/{questionId}")
    public ResponseEntity<?> deleteAnswer(@PathVariable Long quizzAttemptId,@PathVariable Long questionId) {
        QuizzAttempt quizzAttempt = quizzAttemptRepository.findById(quizzAttemptId).get();
        Question question = questionRepository.findById(questionId).get();
        
        quizzAttemptServices.deleteAnswer(quizzAttempt, question);
        Map<String, String> response = new HashMap<>();
        response.put("message", "answer deleted");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{quizzAttemptId}/scoring")
    public ResponseEntity<Map<String,Integer>> getScoring(@PathVariable Long quizzAttemptId) {
        QuizzAttempt quizzAttempt = quizzAttemptRepository.findById(quizzAttemptId).get();
        return ResponseEntity.ok(quizzAttemptServices.getScoring(quizzAttempt));
    }
    @PatchMapping("{quizzAttemptId}/report_cheating_attempt")
    public ResponseEntity<?> report_cheating_attempt(@PathVariable long quizzAttemptId){
        QuizzAttempt quizzAttempt = quizzAttemptRepository.findById(quizzAttemptId).get();
        quizzAttempt.setCheated(true);
        quizzAttemptRepository.save(quizzAttempt);
        Map<String, String> response = new HashMap<>();
        response.put("message", "cheating attempt reported");
        return ResponseEntity.ok(response);
    }

    
    
}
