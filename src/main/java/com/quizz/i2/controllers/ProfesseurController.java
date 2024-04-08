package com.quizz.i2.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizz.i2.entities.Professeur;
import com.quizz.i2.entities.Quizz;
import com.quizz.i2.repositories.ProfesseurRepository;
import com.quizz.i2.repositories.QuizzRepository;
import com.quizz.i2.services.ProfesseurServices;

@RestController
@RequestMapping("/professeurs")
public class ProfesseurController {
    @Autowired
    private ProfesseurServices profServices;
    @Autowired
    private ProfesseurRepository profRep;
    @Autowired
    private QuizzRepository quizzRep;

    @PostMapping("/save")
    public ResponseEntity<?> saveProf(@RequestBody Professeur professeur) {
        Professeur prof;
        try{
            prof = profServices.enregistrerProfesseur(professeur);
        }catch(RuntimeException exp){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exp.getMessage());
        }
        return ResponseEntity.ok(prof);
    }
    @PostMapping("/connect")
    public ResponseEntity<?> connectProfesseur(@RequestBody Map<String, String> RequestBody) {
        String username = RequestBody.get("username");
        String password = RequestBody.get("password");
        Optional<Professeur> prof = profRep.findByUsernameAndPassword(username, password);
        if(! prof.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("professeur not found");
        return ResponseEntity.ok(prof);
    }
    @PostMapping("/update")
    public ResponseEntity<?> modifierProfesseur(@RequestBody Professeur professeur) {
        Optional<Professeur> prof = profRep.findById(professeur.getId());
        if (! prof.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("professeur not exists");
        }
        Professeur newProf;
        try{
            newProf = profServices.modifierProfesseur(professeur);
        }catch(RuntimeException exp){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exp.getMessage());
        }
        return ResponseEntity.ok(newProf);
    }

    @PostMapping("/{professeurId}/createquizz")
    public ResponseEntity<?> createQuizz(@PathVariable Long professeurId, @RequestBody Quizz quizz) {
        Optional<Professeur> prof = profRep.findById(professeurId);
        if(! prof.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("professeur not exists");
        profServices.createQuizz(prof.get(), quizz);
        return ResponseEntity.ok("quizz created");
    }

    @DeleteMapping("/{professeurId}/deletequizz/{quizzId}")
    public ResponseEntity<?> deleteQuizz(@PathVariable Long professeurId, @PathVariable Long quizzId) {
        Optional<Professeur> prof = profRep.findById(professeurId);
        Optional<Quizz> quizz = quizzRep.findById(quizzId);
        if(! prof.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("professeur not exists");
        if(! quizz.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quizz not exists");
        try{
            profServices.deleteQuizz(prof.get(), quizz.get());
        }catch(RuntimeException exp){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exp.getMessage());
        }
        return ResponseEntity.ok("quizz deleted");
    }
}
