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
@Entity(name = "niveau")
@Table(name = "niveau")
public class Niveau {
    @Schema(description = "identifiant du niveau", example = "1")
    @Id
    @GeneratedValue
    @Column(name = "id_niveau")
    private Integer idNiveau;
    @Schema(description = "libellé du niveau", required = true, example = "Master 1")
    @Column(name = "libelle_niveau")
    private String libelleNiveau;

    @Override
    public String toString() {
        return "Niveau{" +
                "idNiveau=" + idNiveau +
                ", libelleNiveau='" + libelleNiveau + '\'' +
                '}';
    }
}
