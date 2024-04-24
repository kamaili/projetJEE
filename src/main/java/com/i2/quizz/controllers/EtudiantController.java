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
        Etudiant etudiant;
        try{
            etudiant = studentService.SaveEtudiant(student);
        }catch(RuntimeException exp){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exp.getMessage()); // username already exists
        }
        return ResponseEntity.ok(etudiant.toDto());
    }
    @PostMapping("/connect")
    public ResponseEntity<?> connectStudent(@RequestBody Map<String, String> RequestBody) {
        String username = RequestBody.get("username");
        String password = RequestBody.get("password");
        Optional<Etudiant> etudiant = etudiantRep.findByUsernameAndPassword(username, password);
        if(! etudiant.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("student not found");
        etudiant.get().setRememberMeToken(generateRememberMeToken());
        Etudiant etud = etudiantRep.save(etudiant.get());
        return ResponseEntity.ok(etud.toDto());
    }
    @PostMapping("/modify")
    public ResponseEntity<?> modifyStudent(@RequestBody Etudiant student) {
        Optional<Etudiant> etud = etudiantRep.findById(student.getId());
        if (! etud.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Student not exists");
        }
        Etudiant newEtudiant;
        try{
            newEtudiant = studentService.modifyEtudiant(student);
        }catch(RuntimeException exp){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exp.getMessage());
        }
        return ResponseEntity.ok(newEtudiant);
    }

    @GetMapping("/score")
    public ResponseEntity<?> consulterScore(@RequestParam Long etudiantId, @RequestParam Long quizzId) {
        Optional<Etudiant> etudiant = etudiantRep.findById(etudiantId);
        if (!etudiant.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Etudiant n'existe pas");
        Optional<Quizz> quizz = quizzRep.findById(quizzId);
        if (!quizz.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quizz n'existe pas");

        int score = studentService.consulterScore(etudiant.get(), quizz.get());
        return ResponseEntity.ok(score);
    }
    @GetMapping("/{etudiantId}/taken_quizzes")
    public ResponseEntity<?> getQuizzes(@PathVariable Long etudiantId) {
        Optional<Etudiant> etudiant = etudiantRep.findById(etudiantId);
        if(! etudiant.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("etudiant not exists");
        List<QuizzDto> list_quizzes = new ArrayList<>();
        for(QuizzAttempt quizzAttempt:etudiant.get().getTakenQuizzes())
            list_quizzes.add(quizzAttempt.getQuizz().toDto());
        return ResponseEntity.ok(list_quizzes);
    }

   








    private String generateRememberMeToken() {
        // Generate a random token (e.g., UUID)
        return UUID.randomUUID().toString();
    }
}
