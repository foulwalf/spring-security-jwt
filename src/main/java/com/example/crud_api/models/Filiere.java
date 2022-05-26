package com.example.crud_api.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "filiere")
@Table(name = "filiere")
public class Filiere {
    @Schema(description = "identifiant de la filière", example = "1")
    @Id
    @GeneratedValue
    @Column(name = "id_filiere")
    private Integer idFiliere;
    @Schema(description = "libellé de la filière", required = true, example = "Informatique")
    @Column(name = "libelle_filiere")
    private String libelleFiliere;

    @Override
    public String toString() {
        return "Filiere{" +
                "id=" + idFiliere +
                ", libelleFiliere='" + libelleFiliere + '\'' +
                '}';
    }
}
