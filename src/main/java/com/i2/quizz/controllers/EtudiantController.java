package com.i2.quizz.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.i2.quizz.DTO.EtudiantDto;
import com.i2.quizz.DTO.QuizzAttemptDto;
import com.i2.quizz.DTO.QuizzDto;
import com.i2.quizz.entities.Etudiant;
import com.i2.quizz.entities.Quizz;
import com.i2.quizz.entities.QuizzAttempt;
import com.i2.quizz.repositories.EtudiantRepository;
import com.i2.quizz.repositories.QuizzAttemptRepository;
import com.i2.quizz.repositories.QuizzRepository;
import com.i2.quizz.services.EtudiantServices;

@RestController
@RequestMapping("/etudiants")
public class EtudiantController {
    @Autowired
    private EtudiantServices studentService;
    @Autowired
    private EtudiantRepository etudiantRep;
    @Autowired
    private QuizzRepository quizzRep;


    
    @PostMapping("/save")
    public ResponseEntity<?> saveStudent(@RequestBody Etudiant student) {
        if(etudiantRep.existsByUsername(student.getUsername()))
        {
            Map<String, String> response = new HashMap<>();
            response.put("message", "username already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        Etudiant etudiant = studentService.SaveEtudiant(student);
        return ResponseEntity.ok(etudiant.toDto());
    }
    @PostMapping("/connect")
    public ResponseEntity<?> connectStudent(@RequestBody Map<String, String> RequestBody) {
        String username = RequestBody.get("username");
        String password = RequestBody.get("password");
        Optional<Etudiant> etudiant = etudiantRep.findByUsernameAndPassword(username, password);
        if(! etudiant.isPresent()){
            Map<String, String> response = new HashMap<>();
            response.put("message", "Wrong username or password");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(etudiant.get().toDto());
    }
    @PostMapping("/modify")
    public ResponseEntity<?> modifyStudent(@RequestBody Etudiant student) {
        
        if(etudiantRep.existsByUsername(student.getUsername()))
        {
            Map<String, String> response = new HashMap<>();
            response.put("message", "username already used");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        Etudiant newEtudiant = studentService.modifyEtudiant(student);
        return ResponseEntity.ok(newEtudiant);
    }

    @GetMapping("/score")
    public ResponseEntity<?> consulterScore(@RequestParam Long etudiantId, @RequestParam Long quizzId) {
        Etudiant etudiant = etudiantRep.findById(etudiantId).get();
        Quizz quizz = quizzRep.findById(quizzId).get();
        int score = studentService.consulterScore(etudiant, quizz);
        return ResponseEntity.ok(score);
    }
    @GetMapping("/{etudiantId}/taken_quizzes")
    public ResponseEntity<?> getQuizzes(@PathVariable Long etudiantId) {
        Etudiant etudiant = etudiantRep.findById(etudiantId).get();
        List<QuizzDto> list_quizzes = new ArrayList<>();
        for(QuizzAttempt quizzAttempt:etudiant.getTakenQuizzes())
            list_quizzes.add(quizzAttempt.getQuizz().toDto());
        return ResponseEntity.ok(list_quizzes);
    }

}
