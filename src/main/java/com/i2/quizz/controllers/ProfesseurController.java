package com.i2.quizz.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i2.quizz.DTO.QuizzDto;
import com.i2.quizz.entities.Professeur;
import com.i2.quizz.entities.Quizz;
import com.i2.quizz.repositories.ProfesseurRepository;
import com.i2.quizz.repositories.QuizzRepository;
import com.i2.quizz.services.ProfesseurServices;
import org.springframework.web.bind.annotation.GetMapping;


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
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exp.getMessage());  // username already exists
        }
        return ResponseEntity.ok(prof.toDto());
    }
    @PostMapping("/connect")
    public ResponseEntity<?> connectProfesseur(@RequestBody Map<String, String> RequestBody) {
        String username = RequestBody.get("username");
        String password = RequestBody.get("password");
        Optional<Professeur> prof = profRep.findByUsernameAndPassword(username, password);
        if(! prof.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("professeur not found");
        prof.get().setRememberMeToken(generateRememberMeToken());
        profRep.save(prof.get());
        return ResponseEntity.ok(prof.get().toDto());
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

    

    @GetMapping("/{professeurId}/createdquizzes")
    public ResponseEntity<?> getQuizzes(@PathVariable Long professeurId) {
        Optional<Professeur> professeur = profRep.findById(professeurId);
        if(! professeur.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("professeur not exists");
        List<QuizzDto> list_quizzes = new ArrayList<>();
        for(Quizz quizz:professeur.get().getCreatedQuizzes())
            list_quizzes.add(quizz.toDto());
        return ResponseEntity.ok(list_quizzes);
    }
    









    private String generateRememberMeToken() {
        // Generate a random token (e.g., UUID)
        return UUID.randomUUID().toString();
    }
}
