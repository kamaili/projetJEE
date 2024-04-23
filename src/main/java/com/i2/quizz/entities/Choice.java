package com.i2.quizz.entities;

import com.i2.quizz.DTO.ChoiceDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "choices")
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private Boolean isCorrect;
    @ManyToOne
    private Question question;

     @Override
    public String toString(){return id+" "+text+" "+" "+isCorrect+" "+question.getId();}

    public ChoiceDto toDto(){
        ChoiceDto dto = new ChoiceDto(id, text, isCorrect, question.getId());
        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        return ((Choice) o).id.equals(this.id);
    }
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
