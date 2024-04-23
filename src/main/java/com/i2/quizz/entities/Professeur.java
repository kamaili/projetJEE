package com.i2.quizz.entities;

import java.util.ArrayList;
import java.util.List;

import com.i2.quizz.DTO.ProfesseurDto;

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
@DiscriminatorValue(value = "PROF")
public class Professeur extends Utilisateur{

    private String departement;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "professeur")
    private List<Quizz> createdQuizzes = new ArrayList<>();

    public ProfesseurDto toDto(){
        ProfesseurDto dto = new ProfesseurDto();
        dto.setId(super.getId());
        dto.setDepartement(departement);
        dto.setEmail(super.getEmail());
        dto.setEtablissement(super.getEtablissement());
        dto.setGender(super.getGender());
        dto.setFirstname(super.getFirstname());
        dto.setLastname(super.getLastname());
        // dto.setPassword(super.getPassword());  pas utile
        dto.setRememberMeToken(super.getRememberMeToken());
        dto.setUsername(super.getUsername());
        for(Quizz quizz:createdQuizzes)
            dto.getCreatedQuizzes().add(quizz.toDto());
        return dto;
        
    }
}
