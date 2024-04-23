package com.i2.quizz.controllers;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.i2.quizz.DTO.EtudiantDto;
import com.i2.quizz.DTO.QuestionDto;
import com.i2.quizz.entities.Etudiant;
import com.i2.quizz.entities.Professeur;
import com.i2.quizz.entities.Question;
import com.i2.quizz.entities.Quizz;
import com.i2.quizz.entities.QuizzAttempt;
import com.i2.quizz.repositories.EtudiantRepository;
import com.i2.quizz.repositories.ProfesseurRepository;
import com.i2.quizz.repositories.QuestionRepository;
import com.i2.quizz.repositories.QuizzAttemptRepository;
import com.i2.quizz.repositories.QuizzRepository;
import com.i2.quizz.services.EtudiantServices;
import com.i2.quizz.services.QuizzServices;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    @Autowired
    private ProfesseurRepository profRep;
    @Autowired
    private EtudiantRepository etudiantRep;
    @Autowired
    private QuizzAttemptRepository quizzAttemptRepository;
    @Autowired
    private EtudiantServices studentService;

    @PatchMapping("/startquizz/{quizzId}")
    public ResponseEntity<?> setQuizzStatus(@PathVariable Long quizzId) {
        Quizz quizz = quizzRepository.findById(quizzId).get();

            quizzServices.commencerQuizz(quizz);
        return ResponseEntity.ok(Map.of("message","Quizz started"));
    }
    @GetMapping("/is_started/{quizzId}")
    public ResponseEntity<Boolean> checkQuizzStarted(@PathVariable Long quizzId) {
        Quizz quizz = quizzRepository.findById(quizzId).get();
        return ResponseEntity.ok(quizz.isStarted());
    }

    @PostMapping("/{professeurId}/createquizz")
    public ResponseEntity<?> createQuizz(@PathVariable Long professeurId, @RequestBody Map<String,Object> quizzData) {
        Optional<Professeur> prof = profRep.findById(professeurId);
        if(! prof.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("professeur not exists");
        Quizz quizz = quizzServices.createQuizz(prof.get(), quizzData);
        return ResponseEntity.ok(quizz.toDto());
    }
    @DeleteMapping("/{professeurId}/deletequizz/{quizzId}")
    public ResponseEntity<?> deleteQuizz(@PathVariable Long professeurId, @PathVariable Long quizzId) {
        Optional<Professeur> prof = profRep.findById(professeurId);
        Optional<Quizz> quizz = quizzRepository.findById(quizzId);
        if(! prof.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("professeur not exists");
        if(! quizz.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quizz not exists");
        try{
            quizzServices.deleteQuizz(prof.get(), quizz.get());
        }catch(RuntimeException exp){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exp.getMessage());
        }
        return ResponseEntity.ok(Map.of("message","quizz deleted"));
    }

    @PostMapping("{quizzId}/createquestion")
    public ResponseEntity<?> creerQuestion(@PathVariable Long quizzId, @RequestBody Question question ) {
        Optional<Quizz> quizz = quizzRepository.findById(quizzId);
        if(! quizz.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("quizz not found");
        QuestionDto createdQuestion = quizzServices.ajouterQuestion(quizz.get(), question, question.getChoices());
    
        return ResponseEntity.ok(createdQuestion);
    }
    @PatchMapping("open/{quizzId}")
    public ResponseEntity<?> open(@PathVariable Long quizzId) {
        quizzServices.open(quizzRepository.findById(quizzId).get());
        return ResponseEntity.ok(Map.of("message","opened"));
    }
    @GetMapping("joinedStudents/{quizzId}")
    public ResponseEntity<List<EtudiantDto>> getJoined(@PathVariable Long quizzId) {
        Quizz quizz = quizzRepository.findById(quizzId).get();
        List<EtudiantDto> joinedStudents = quizzServices.joinedStudents(quizz);
        return ResponseEntity.ok(joinedStudents);
    }
    @PatchMapping("updatequestion")
    public ResponseEntity<?> updateQuestion(@RequestBody QuestionDto newQuestion ) {
        System.out.println("***********************************");
        Optional<Question> question = questionRepository.findById(newQuestion.getId());
        if(! question.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("question not found");
        quizzServices.modifierQuestion(newQuestion);
    
        return ResponseEntity.ok(Map.of("message","question added"));
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
        
        return ResponseEntity.ok(Map.of("message","question deleted from the quizz"));
    }
    @GetMapping("{quizzId}/listquestions")
    public ResponseEntity<?> getQuestions(@PathVariable Long quizzId) {
        Optional<Quizz> quizz = quizzRepository.findById(quizzId);
        if(! quizz.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("quizz not found");
        

        return ResponseEntity.ok(quizz.get().toDto().getQuestions());
    }
    @PostMapping("{etudiantId}/joinquizz/{token}")
    // must add token <key,value> to the student json object
    public ResponseEntity<?> rejoindreQuizz(@PathVariable Long etudiantId, @PathVariable String token) {

        Etudiant etudiant = etudiantRep.findById(etudiantId).get();
        Optional<Quizz> quizz = quizzRepository.findByToken(token);
        if( ! quizz.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        int participantsNbr = quizzAttemptRepository.findByQuizz(quizz.get()).size();

        if (participantsNbr >= quizz.get().getMaxParticipations())
        return ResponseEntity.status(HttpStatus.CONFLICT).body(null); //max join reached

        Optional<QuizzAttempt> existQuizzAttempt = quizzAttemptRepository.findByEtudiantIdAndQuizzId(etudiant.getId(), quizz.get().getId());

        if(existQuizzAttempt.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null); // already joined
        
        QuizzAttempt quizzAttempt = studentService.rejoindreQuizz(etudiant, quizz.get());
        Map<String,Object> response = new HashMap<>();
        response.put("quizz", quizz.get().toDto());
        response.put("quizzAttempt", quizzAttempt.toDto());
        return ResponseEntity.ok(response);
    }

    @PostMapping("{etudiantId}/quitquizz/{quizzId}")
    // must add quizz_id <key,value> to the student json object
    public ResponseEntity<?> quitterQuizz(@PathVariable Long etudiantId, @PathVariable Long quizzId) {
        Etudiant etudiant = etudiantRep.findById(etudiantId).get();
        Quizz quizz = quizzRepository.findById(quizzId).get();
        studentService.QuitterQuiz(etudiant, quizz);

        return ResponseEntity.ok(Map.of("message", "quizz quited"));
    }


    @GetMapping("progress/{quizzId}")
    public ResponseEntity<List<Map<String,Object>>> getProgress(@PathVariable Long quizzId) {
        Quizz quizz = quizzRepository.findById(quizzId).get();

        return ResponseEntity.ok(quizzServices.getProgress(quizz));
    }
    @GetMapping("{quizzId}/get_attempt/{etudiantId}")
    public ResponseEntity<QuizzAttempt> geAttempt(@PathVariable Long quizzId, @PathVariable Long etudiantId) {
        return ResponseEntity.ok(quizzAttemptRepository.findByEtudiantIdAndQuizzId(etudiantId, quizzId).get());
    }
    
    


    
}

// à completer