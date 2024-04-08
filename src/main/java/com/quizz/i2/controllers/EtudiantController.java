package com.quizz.i2.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quizz.i2.entities.Etudiant;
import com.quizz.i2.entities.Quizz;
import com.quizz.i2.entities.QuizzAttempt;
import com.quizz.i2.repositories.EtudiantRepository;
import com.quizz.i2.repositories.QuizzAttemptRepository;
import com.quizz.i2.repositories.QuizzRepository;
import com.quizz.i2.services.EtudiantServices;

@RestController
@RequestMapping("/etudiants")
public class EtudiantController {
    @Autowired
    private EtudiantServices studentService;
    @Autowired
    private EtudiantRepository etudiantRep;
    @Autowired
    private QuizzRepository quizzRep;
    @Autowired
    private QuizzAttemptRepository quizzAttemptRepository;

    @PostMapping("/save")
    public ResponseEntity<?> saveStudent(@RequestBody Etudiant student) {
        Etudiant etudiant;
        try{
            etudiant = studentService.SaveEtudiant(student);
        }catch(RuntimeException exp){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exp.getMessage());
        }
        return ResponseEntity.ok(etudiant);
    }
    @PostMapping("/connect")
    public ResponseEntity<?> connectStudent(@RequestBody Map<String, String> RequestBody) {
        String username = RequestBody.get("username");
        String password = RequestBody.get("password");
        Optional<Etudiant> etudiant = etudiantRep.findByUsernameAndPassword(username, password);
        if(! etudiant.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("student not found");
        return ResponseEntity.ok(etudiant);
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

    @PostMapping("{etudiantId}/joinquizz/{token}")
    // must add token <key,value> to the student json object
    public ResponseEntity<?> rejoindreQuizz(@PathVariable Long etudiantId, @PathVariable String token) {

        Optional<Etudiant> etudiant = etudiantRep.findById(etudiantId);
        Optional<Quizz> quizz = quizzRep.findByToken(token);
        if (!etudiant.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Etudiant n'existe pas");
        if (!quizz.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quizz n'existe pas");
        
         int participantsNbr = quizzAttemptRepository.findByQuizz(quizz.get()).size();
        if (participantsNbr >= quizz.get().getMaxParticipations())
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("max join reached");
        
        QuizzAttempt quizzAttempt;
        try {
            quizzAttempt = studentService.rejoindreQuizz(etudiant.get(), quizz.get());
        } catch (RuntimeException exp) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exp.getMessage());
        }
        Map<String,String> response = new HashMap<>();
        response.put("id", "123");
        response.put("quizzTitle", quizzAttempt.getQuizz().getTitle());
        response.put("teacherName", quizzAttempt.getQuizz().getProfesseur().getUsername());
        response.put("duration", ""+quizzAttempt.getQuizz().getDuration());
        response.put("questionsCount", ""+quizzAttempt.getQuizz().getQuestions().size());
        response.put("maxParticipations", ""+quizzAttempt.getQuizz().getMaxParticipations());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/quitquizz")
    // must add quizz_id <key,value> to the student json object
    public ResponseEntity<?> quitterQuizz(@RequestBody Map<String, String> requestBody) {
        Long etudiantId = Long.parseLong(requestBody.get("id"));
        Long quizzId = Long.parseLong(requestBody.get("quizz_id"));

        Optional<Etudiant> etudiant = etudiantRep.findById(etudiantId);
        Optional<Quizz> quizz = quizzRep.findById(quizzId);
        if (!etudiant.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Etudiant n'existe pas");
        if (!quizz.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quizz n'existe pas");
        try {
            studentService.QuitterQuiz(etudiant.get(), quizz.get());
        } catch (RuntimeException exp) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exp.getMessage());
        }
        return ResponseEntity.ok("etudiant "+etudiantId+" a quitté quizz "+quizzId);
    }
}
