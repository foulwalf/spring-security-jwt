package com.example.crud_api.models;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "etudiant")
@Table(name = "etudiant")
public class Etudiant {
    @Schema(description = "Identifiant de l'étudiant", example = "1")
    @Id
    @GeneratedValue
    @Column(name = "id_etudiant")
    private Integer idEtudiant;
    @Schema(description = "Nom de l'étudiant", example = "Koffi")
    @Column(name = "nom_etudiant")
    private String nomEtudiant;
    @Schema(description = "Prénom de l'étudiant", example = "Ali Roger")
    @Column(name = "prenom_etudiant")
    private String prenomEtudiant;
    @Schema(description = "Adresse de l'étudiant", example = "Cocody, Riviera palmeraie")
    @Column(name = "adresse_etudiant")
    private String adresseEtudiant;
    @Schema(description = "Contact de l'étudiant", example = "0890765609")
    @Column(name = "contact_etudiant")
    private String contactEtudiant;
    @Schema(description = "Email de l'étudiant", example = "koffi.roger@gmail.com")
    @Column(name = "email_etudiant")
    private String emailEtudiant;
    @Schema(description = "Filiere de l'étudiant", implementation = Filiere.class)
    @ManyToOne
    @JoinColumn(name = "id_filiere")
    private Filiere filiere;
    @Schema(description = "Niveau de l'étudiant", implementation = Niveau.class)
    @ManyToOne
    @JoinColumn(name = "id_niveau")
    private Niveau niveau;

    @Override
    public String toString() {
        return "Etudiant{" +
                "id=" + idEtudiant +
                ", nomEtudiant='" + nomEtudiant + '\'' +
                ", prenomEtudiant='" + prenomEtudiant + '\'' +
                ", adresseEtudiant='" + adresseEtudiant + '\'' +
                ", contactEtudiant='" + contactEtudiant + '\'' +
                ", emailEtudiant='" + emailEtudiant + '\'' +
                ", filiere=" + filiere +
                ", niveau=" + niveau +
                '}';
    }
}
