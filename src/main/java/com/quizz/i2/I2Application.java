package com.quizz.i2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.quizz.i2.entities.Etudiant;
import com.quizz.i2.entities.Professeur;
import com.quizz.i2.entities.Quizz;
import com.quizz.i2.repositories.EtudiantRepository;
import com.quizz.i2.repositories.ProfesseurRepository;

@SpringBootApplication
public class I2Application implements CommandLineRunner {
	@Autowired
	ProfesseurRepository pRep;
	@Autowired
	EtudiantRepository eRep;

	public static void main(String[] args) {
		SpringApplication.run(I2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Professeur prof = new Professeur();
		prof.setUsername("ben aouicha");
		prof.setPassword("123456");
		pRep.save(prof);
		Etudiant etud = new Etudiant();
		etud.setUsername("aymen");
		etud.setPassword("123456");
		eRep.save(etud);
	}

}
