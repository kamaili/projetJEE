package com.i2.quizz.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i2.quizz.DTO.ChoiceDto;
import com.i2.quizz.DTO.EtudiantDto;
import com.i2.quizz.DTO.QuestionDto;
import com.i2.quizz.entities.Answer;
import com.i2.quizz.entities.Choice;
import com.i2.quizz.entities.Etudiant;
import com.i2.quizz.entities.Professeur;
import com.i2.quizz.entities.Question;
import com.i2.quizz.entities.Quizz;
import com.i2.quizz.entities.QuizzAttempt;
import com.i2.quizz.repositories.ChoiceRepository;
import com.i2.quizz.repositories.EtudiantRepository;
import com.i2.quizz.repositories.ProfesseurRepository;
import com.i2.quizz.repositories.QuestionRepository;
import com.i2.quizz.repositories.QuizzAttemptRepository;
import com.i2.quizz.repositories.QuizzRepository;


@Service
public class QuizzServicesImpl implements QuizzServices{

    @Autowired
    QuizzRepository quizzRep;
    @Autowired
    QuestionRepository questionRep;
    @Autowired
    ProfesseurRepository profRep;
    @Autowired
    ChoiceRepository choicesRep;
    @Autowired
    QuizzAttemptRepository quizzAttemptRepository;
    @Autowired
    EtudiantRepository etudiantRepository;
        

    
    public void modifierQuestion(QuestionDto newQuestion){
        Question question = questionRep.findById(newQuestion.getId()).get();
        question.getChoices().clear();
        for (Choice choice : question.getChoices()) {
            choicesRep.deleteChoiceById(choice.getId());
        }
           
          question.setText(newQuestion.getText());
        for (ChoiceDto choiceDto : newQuestion.getChoices()) {
            Choice choice = new Choice(null, choiceDto.getText(), choiceDto.getIsCorrect(), question);
            question.getChoices().add(choice);
        }
    
        questionRep.save(question);
    }
    @Transactional
    public Quizz createQuizz(Professeur prof, Map<String,Object> quizzData){
        String title = (String) quizzData.get("title");
        int duration = (int) quizzData.get("duration");
        String token = (String) quizzData.get("token");
        int maxParticipations = (int) quizzData.get("maxParticipations");

        Quizz quizz = new Quizz(null, title, duration, token, false,false,false, maxParticipations, prof, null);
        quizz.setProfesseur(prof);
        List<Question> questions = new ArrayList<>();
        for(Map<String,Object> questionData: (ArrayList<Map<String,Object>>) quizzData.get("questions")){
            System.out.println(questionData);
            Question question = new Question();
            question.setText((String) questionData.get("text"));

            List<Choice> choices = new ArrayList<>();
            for(Map<String,Object> choiceData: (List<Map<String, Object>>) questionData.get("choices")){
                Choice choice = new Choice(null, (String) choiceData.get("text"), (boolean) choiceData.get("isCorrect"), question);
                choices.add(choice);
            }
            question.setChoices(choices);
            question.setQuizz(quizz);
            questionRep.save(question);
            questions.add(question);
        }
        quizz.setQuestions(questions);
        quizzRep.save(quizz);
        prof.getCreatedQuizzes().add(quizz);
        profRep.save(prof);
        return quizz;
    }
    public void deleteQuizz(Professeur prof, Quizz quizz){
        prof.getCreatedQuizzes().remove(quizz);
        quizzRep.delete(quizz);
        profRep.save(prof);
    }

    @Override
    public void commencerQuizz(Quizz quizz) {
        quizz.setStarted(true);
        quizzRep.save(quizz);
        
        new Thread(new Runnable() {

            // arreter le quizz apres une minute de la fin de délais (prendre en compte la petite latence)
            @Override
            public void run() {
                try {
                    Thread.sleep((quizz.getDuration()+1) * 60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                arreterQuizz(quizz);
            }
            
        }).start();
    }


    @Override
    public void arreterQuizz(Quizz quizz) {
        quizz.setFinished(true);
        quizzRep.save(quizz);
    }

    @Override
    public QuestionDto ajouterQuestion(Quizz quizz, Question question, List<Choice> choices) {
        question.setQuizz(quizz);
        for (Choice choice : choices) {
            choice.setQuestion(question);
        }
        question.setChoices(choices);
        questionRep.save(question);
        quizz.getQuestions().add(question);
        quizzRep.save(quizz);
        return question.toDto();
    }

    @Override
    public void supprimerQuestion(Quizz quizz, Question question) {
        quizz.getQuestions().remove(question);
        quizzRep.save(quizz);
        questionRep.delete(question);
    }
    @Override
    public void open(Quizz quizz){
        quizz.setOpenToJoin(true);
        quizzRep.save(quizz);
    }
    @Override
    public List<EtudiantDto> joinedStudents(Quizz quizz){
        List<QuizzAttempt> quizzAttempts = quizzAttemptRepository.findByQuizz(quizz);
        List<EtudiantDto> joinedEtudiants = new ArrayList<>();
        for(QuizzAttempt qAtt:quizzAttempts){
            EtudiantDto etudiant = etudiantRepository.findById(qAtt.getEtudiant().getId()).get().toDto();
            joinedEtudiants.add(etudiant);}
        return joinedEtudiants;    
    }
    @Override
    public List<Map<String,Object>> getProgress(Quizz quizz){
        List<QuizzAttempt> quizzAttempts = quizzAttemptRepository.findByQuizz(quizz);
        List<Map<String,Object>> result = new ArrayList<>();
        for(QuizzAttempt quizzAttempt: quizzAttempts){
            EtudiantDto etudiantDto = quizzAttempt.getEtudiant().toDto();
            List<Boolean> progress = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("student", etudiantDto);
            map.put("cheated", quizzAttempt.isCheated());
            List<Answer> listAnswers = quizzAttempt.getAnswers();
            for(Answer answer : listAnswers){
                List <Choice> choices = new ArrayList<>();
                for(Choice choice : answer.getSelectedChoices())
                    choices.add(choicesRep.findById(choice.getId()).get());
                List <Choice> correctChoices = choicesRep.findByQuestionAndIsCorrect(answer.getQuestion(), true);
                progress.add(correctChoices.containsAll(choices));
            }
            map.put("progress", progress);
            result.add(map);
        }
        return result;
        
    }
}
