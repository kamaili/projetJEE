package com.i2.quizz.controllers;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i2.quizz.DTO.EtudiantDto;
import com.i2.quizz.DTO.QuestionDto;
import com.i2.quizz.DTO.QuizzAttemptDto;
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
        Map<String, String> response = new HashMap<>();
        response.put("message", "Quizz started");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/is_started/{quizzId}")
    public ResponseEntity<Boolean> checkQuizzStarted(@PathVariable Long quizzId) {
        Quizz quizz = quizzRepository.findById(quizzId).get();
        return ResponseEntity.ok(quizz.isStarted());
    }

    @PostMapping("/{professeurId}/createquizz")
    public ResponseEntity<?> createQuizz(@PathVariable Long professeurId, @RequestBody Map<String,Object> quizzData) {
        Professeur prof = profRep.findById(professeurId).get();
        Quizz quizz = quizzServices.createQuizz(prof, quizzData);
        return ResponseEntity.ok(quizz.toDto());
    }
    @DeleteMapping("/{professeurId}/deletequizz/{quizzId}")
    public ResponseEntity<?> deleteQuizz(@PathVariable Long professeurId, @PathVariable Long quizzId) {
        Professeur prof = profRep.findById(professeurId).get();
        Quizz quizz = quizzRepository.findById(quizzId).get();
        quizzServices.deleteQuizz(prof, quizz);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "deleted");
        return ResponseEntity.ok(response);
    }

    @PostMapping("{quizzId}/createquestion")
    public ResponseEntity<?> creerQuestion(@PathVariable Long quizzId, @RequestBody Question question ) {
        Quizz quizz = quizzRepository.findById(quizzId).get();
        QuestionDto createdQuestion = quizzServices.ajouterQuestion(quizz, question, question.getChoices());
    
        return ResponseEntity.ok(createdQuestion);
    }
    @PatchMapping("open/{quizzId}")
    public ResponseEntity<?> open(@PathVariable Long quizzId) {
        quizzServices.open(quizzRepository.findById(quizzId).get());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Quizz opened");
        return ResponseEntity.ok(response);
    }
    @GetMapping("joinedStudents/{quizzId}")
    public ResponseEntity<List<EtudiantDto>> getJoined(@PathVariable Long quizzId) {
        Quizz quizz = quizzRepository.findById(quizzId).get();
        List<EtudiantDto> joinedStudents = quizzServices.joinedStudents(quizz);
        return ResponseEntity.ok(joinedStudents);
    }
    @PatchMapping("updatequestion")
    public ResponseEntity<?> updateQuestion(@RequestBody QuestionDto newQuestion ) {
        quizzServices.modifierQuestion(newQuestion);
    
        Map<String, String> response = new HashMap<>();
        response.put("message", "Question added");
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("{quizzId}/deletequestion/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long quizzId, @PathVariable Long questionId){
        Quizz quizz = quizzRepository.findById(quizzId).get();
        Question question = questionRepository.findByIdAndQuizzId(questionId,quizzId).get();
        quizzServices.supprimerQuestion(quizz, question);        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Question deleted");
        return ResponseEntity.ok(response);
    }
    @GetMapping("{quizzId}/listquestions")
    public ResponseEntity<?> getQuestions(@PathVariable Long quizzId) {
        Quizz quizz = quizzRepository.findById(quizzId).get();
        

        return ResponseEntity.ok(quizz.toDto().getQuestions());
    }
    @PostMapping("{etudiantId}/joinquizz/{token}")
    public ResponseEntity<?> rejoindreQuizz(@PathVariable Long etudiantId, @PathVariable String token) {

        Etudiant etudiant = etudiantRep.findById(etudiantId).get();
        Optional<Quizz> quizz = quizzRepository.findByToken(token);
        Map<String,Object> response = new HashMap<>();

        if (!quizz.isPresent())
        {
            response.put("message", "verify your token");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if( quizz.get().isStarted() || quizz.get().isFinished())
        {
            response.put("message", "quizz was already started");
            return ResponseEntity.status(HttpStatus.NOT_EXTENDED).body(response);
        }
        if( ! quizz.get().isOpenToJoin())
        {
            response.put("message", "quizz not opened to join yet");
            return ResponseEntity.status(HttpStatus.NOT_EXTENDED).body(response);
        }
        
        int participantsNbr = quizzAttemptRepository.findByQuizz(quizz.get()).size();

        if (participantsNbr >= quizz.get().getMaxParticipations())
        {
            response.put("message", "quizz has reached the maximum join limite");
            return ResponseEntity.status(HttpStatus.NOT_EXTENDED).body(response);
        }

        Optional<QuizzAttempt> existQuizzAttempt = quizzAttemptRepository.findByEtudiantIdAndQuizzId(etudiant.getId(), quizz.get().getId());

        
        if(existQuizzAttempt.isPresent())
        {
            response.put("message", "quizz already joined");
            return ResponseEntity.status(HttpStatus.NOT_EXTENDED).body(response);
        }
        
        QuizzAttempt quizzAttempt = studentService.rejoindreQuizz(etudiant, quizz.get());
        response = new HashMap<>();
        response.put("quizz", quizz.get().toDto());
        response.put("quizzAttempt", quizzAttempt.toDto());
        return ResponseEntity.ok(response);
    }

    @PostMapping("{etudiantId}/quitquizz/{quizzId}")
    public ResponseEntity<?> quitterQuizz(@PathVariable Long etudiantId, @PathVariable Long quizzId) {
        Etudiant etudiant = etudiantRep.findById(etudiantId).get();
        Quizz quizz = quizzRepository.findById(quizzId).get();
        studentService.QuitterQuiz(etudiant, quizz);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Quizz quited");
        return ResponseEntity.ok(response);
    }


    @GetMapping("progress/{quizzId}")
    public ResponseEntity<List<Map<String,Object>>> getProgress(@PathVariable Long quizzId) {
        Quizz quizz = quizzRepository.findById(quizzId).get();

        return ResponseEntity.ok(quizzServices.getProgress(quizz));
    }
    @GetMapping("{quizzId}/get_attempt/{etudiantId}")
    public ResponseEntity<QuizzAttemptDto> geAttempt(@PathVariable Long quizzId, @PathVariable Long etudiantId) {
        return ResponseEntity.ok(quizzAttemptRepository.findByEtudiantIdAndQuizzId(etudiantId, quizzId).get().toDto());
    }
    
    


    
}
