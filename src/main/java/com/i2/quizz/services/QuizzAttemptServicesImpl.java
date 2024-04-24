package com.i2.quizz.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.i2.quizz.DTO.AnswerDto;
import com.i2.quizz.entities.Answer;
import com.i2.quizz.entities.Choice;
import com.i2.quizz.entities.Question;
import com.i2.quizz.entities.QuizzAttempt;
import com.i2.quizz.repositories.AnswerRepository;
import com.i2.quizz.repositories.ChoiceRepository;
import com.i2.quizz.repositories.QuizzAttemptRepository;

@Service
public class QuizzAttemptServicesImpl implements QuizzAttemptServices{

    @Autowired
    private AnswerRepository answerRep;
    @Autowired
    private QuizzAttemptRepository quizzAttemptRep;
    @Autowired
    private ChoiceRepository choicesRep;

    public Answer setAnswer(QuizzAttempt quizzAttempt, Question question, List<Choice> choices) {
        Answer answer = new Answer(null, choices, question, quizzAttempt);
        answerRep.save(answer);
        quizzAttempt.getAnswers().add(answer);
        quizzAttemptRep.save(quizzAttempt);
        updateScore(quizzAttempt);
        return answer;
    }
    public Answer updateAnswer(QuizzAttempt quizzAttempt, Question question, List<Choice> choices) {
        Answer answer = answerRep.findByQuestionIdAndQuizzAttemptId(question.getId(), quizzAttempt.getId());
        answer.getSelectedChoices().clear();
        answer.setSelectedChoices(choices);
        answerRep.save(answer);
        updateScore(quizzAttempt);
        return answer;
    }
    public void deleteAnswer(QuizzAttempt quizzAttempt, Question question) {
        Answer answer = answerRep.findByQuestionIdAndQuizzAttemptId(question.getId(), quizzAttempt.getId());
        answer.getSelectedChoices().clear();
        quizzAttempt.getAnswers().remove(answer);
        answerRep.forcerDelete(answer.getId());
        updateScore(quizzAttempt);
    }

    private void updateScore(QuizzAttempt quizzAttempt){
        int score = 0;
        List<Answer> listAnswers = quizzAttempt.getAnswers();
        for(Answer answer : listAnswers){
            List <Choice> choices = new ArrayList<>();
            for(Choice choice : answer.getSelectedChoices())
                choices.add(choicesRep.findById(choice.getId()).get());

            List <Choice> correctChoices = choicesRep.findByQuestionAndIsCorrect(answer.getQuestion(), true);
            if(correctChoices.containsAll(choices))
                score++;
        }
        quizzAttempt.setScore(score);
        quizzAttemptRep.save(quizzAttempt);
    }
    public Map<String,Integer> getScoring(QuizzAttempt quizzAttempt){
        int score = quizzAttempt.getScore();
        int questionsCount = quizzAttempt.getQuizz().getQuestions().size();
        int rank = 0 ;
        int cheated = quizzAttempt.isCheated() ? 1 : 0;

        List<QuizzAttempt> list = quizzAttemptRep.findByQuizz(quizzAttempt.getQuizz());

        int competitorsCount = list.size();
        if( ! quizzAttempt.getQuizz().isFinished()){
            rank = list.size();
            for(QuizzAttempt qAtt:list){
                if (score < qAtt.getScore())
                    rank ++ ;
            }
        }
        Map<String, Integer> response = new HashMap<>();
        response.put("score", score);
        response.put("questionsCount", questionsCount);
        response.put("competitorsCount", competitorsCount);
        response.put("rank", rank);
        response.put("cheated", cheated);
        return response;    
    }
    


}
