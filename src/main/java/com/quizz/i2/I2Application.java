package com.quizz.i2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.quizz.i2.entities.Answer;
import com.quizz.i2.entities.Choice;
import com.quizz.i2.entities.Etudiant;
import com.quizz.i2.entities.Professeur;
import com.quizz.i2.entities.Question;
import com.quizz.i2.entities.Quizz;
import com.quizz.i2.repositories.EtudiantRepository;
import com.quizz.i2.repositories.ProfesseurRepository;
import com.quizz.i2.services.EtudiantServices;
import com.quizz.i2.services.ProfesseurServices;
import com.quizz.i2.services.QuizzServices;

@SpringBootApplication
public class I2Application implements CommandLineRunner {
	
	@Autowired
	ProfesseurServices pSer;
	@Autowired
	EtudiantServices eSer;
	@Autowired
	QuizzServices qSer;


	public static void main(String[] args) {
		SpringApplication.run(I2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Professeur prof = new Professeur();
		prof.setUsername("ben aouicha");
		prof.setPassword("123456");
		Etudiant etud = new Etudiant();
		etud.setUsername("aymen");
		etud.setPassword("123456");
		eSer.SaveEtudiant(etud);
		Quizz quizz = new Quizz(null, "siwar", 60, "asvddgfr", false, prof, null);
		pSer.enregistrerProfesseur(prof);
		pSer.createQuizz(prof, quizz);
		Question question = new Question();
		
		Choice choice = new Choice(null, "YARN + HDFS", true,question);
		Choice choice2 = new Choice(null, "KAFKA + HDFS", false,question);
		List<Choice> ls = new ArrayList<>();
		ls.add(choice);ls.add(choice2);
		question.setQuizz(quizz);
		question.setChoices(ls);
		question.setText("c'est quoi hadoop");
		qSer.ajouterQuestion(quizz, question);
		
	}

}
