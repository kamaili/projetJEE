package com.quizz.i2.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quizz.i2.entities.Quizz;
import com.quizz.i2.repositories.QuizzAttemptRepository;
import com.quizz.i2.repositories.QuizzRepository;
import com.quizz.i2.services.QuizzServices;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/quizzes")
public class QuizzController {
    @Autowired
    QuizzServices quizzServices;
    @Autowired
    QuizzRepository quizzRepository;

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
}

// à completer