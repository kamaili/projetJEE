 package com.i2.quizz.entities;

import java.util.ArrayList;
import java.util.List;

import com.i2.quizz.DTO.EtudiantDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue(value = "ETUD")
public class Etudiant extends Utilisateur{
    private String classe;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "etudiant")
    private List<QuizzAttempt> takenQuizzes = new ArrayList<>();


    public EtudiantDto toDto(){
        EtudiantDto dto = new EtudiantDto();
        dto.setId(super.getId());
        dto.setClasse(classe);
        dto.setEmail(super.getEmail());
        dto.setEtablissement(super.getEtablissement());
        dto.setGender(super.getGender());
        dto.setFirstname(super.getFirstname());
        dto.setLastname(super.getLastname());
        dto.setUsername(super.getUsername());
        for(QuizzAttempt quizzAttempt:takenQuizzes)
            dto.getTakenQuizzes().add(quizzAttempt.toDto());
        return dto;
        
    }


}
