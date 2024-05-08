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

    @PostMapping("/save")
    public ResponseEntity<?> saveProf(@RequestBody Professeur professeur) {
        if(profRep.existsByUsername(professeur.getUsername()))
        {
            Map<String, String> response = new HashMap<>();
            response.put("message", "username already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        
        Professeur prof= profServices.enregistrerProfesseur(professeur);
        return ResponseEntity.ok(prof.toDto());
    }
    @PostMapping("/connect")
    public ResponseEntity<?> connectProfesseur(@RequestBody Map<String, String> RequestBody) {
        String username = RequestBody.get("username");
        String password = RequestBody.get("password");
        Optional<Professeur> prof = profRep.findByUsernameAndPassword(username, password);
        if(! prof.isPresent()){
            Map<String, String> response = new HashMap<>();
            response.put("message", "Wrong username or password");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(prof.get().toDto());
    }
    @PostMapping("/modify")
    public ResponseEntity<?> modifierProfesseur(@RequestBody Professeur professeur) {
        if(profRep.existsByUsername(professeur.getUsername()))
        {
            Map<String, String> response = new HashMap<>();
            response.put("message", "username already used");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        Professeur newProf = profServices.modifierProfesseur(professeur);
        return ResponseEntity.ok(newProf.toDto());
    }

    

    @GetMapping("/{professeurId}/createdquizzes")
    public ResponseEntity<?> getQuizzes(@PathVariable Long professeurId) {
        Professeur professeur = profRep.findById(professeurId).get();
        List<QuizzDto> list_quizzes = new ArrayList<>();
        for(Quizz quizz:professeur.getCreatedQuizzes())
            list_quizzes.add(quizz.toDto());
        return ResponseEntity.ok(list_quizzes);
    }
    
}
