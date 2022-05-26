package com.example.crud_api.controllers;

import com.example.crud_api.models.Etudiant;
import com.example.crud_api.models.Filiere;
import com.example.crud_api.models.Niveau;
import com.example.crud_api.models.Result;
import com.example.crud_api.repositories.EtudiantRepository;
import com.example.crud_api.repositories.FiliereRepository;
import com.example.crud_api.repositories.NiveauRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/etudiant")
@Tag(name = "etudiant", description = "etudiant controller")
@CrossOrigin(origins = "*")
public class EtudiantController {
    @Autowired
    EtudiantRepository etudiantRepository;
    @Autowired
    FiliereRepository filiereRepository;
    @Autowired
    NiveauRepository niveauRepository;
    /**
     * @param etudiant
     * @return ResponseEntity
     */
    @Operation(summary = "Ajouter un nouvel étudiant", description = "", tags = {"etudiant"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "L'étudiant a bien été enregistré", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "Une erreur s'est produite", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @PostMapping("/add")
    public ResponseEntity<Result<Object>> addEtudiant(@RequestBody Etudiant etudiant) {
        ResponseEntity<Result<Object>> responseEntity;
        etudiantRepository.save(etudiant);
        Optional<Etudiant> addedEtudiant = etudiantRepository.findById(etudiant.getIdEtudiant());
        if (addedEtudiant.isPresent()) {
            responseEntity = new ResponseEntity(new Result<>("L'étudiant a bien été enregistré"), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity(new Result<>("Impossible d'ajouter l'étudiant"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    /**
     * @return ResponseEntity
     */
    @Operation(summary = "Lister tous les étudiants", description = "", tags = {"etudiant"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Il n'y a aucun étudiant dans la base de données", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "200", description = "Liste bien récupérée", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Result.class)))),
            @ApiResponse(responseCode = "500", description = "Une erreur s'est produite", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @GetMapping("/list")
    public ResponseEntity<Result<List<Etudiant>>> getAllEtudiants() {
        ResponseEntity<Result<List<Etudiant>>> responseEntity;
        try {
            List<Etudiant> etudiants = (List<Etudiant>) etudiantRepository.findAll();
            if (etudiants.size() == 0) {
                responseEntity = new ResponseEntity<>(new Result<>("Il n'y a aucun étudiant dans la base de données"), HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>(new Result<>("Liste bien récupérée", etudiants), HttpStatus.OK);
            }
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(new Result<>("Une erreur s'est produite"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @Operation(summary = "Lister tous les étudiants en fonction de la filière", description = "", tags = {"etudiant"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Il n'y a aucun étudiant dans cette filière", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "200", description = "Liste bien récupérée", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Result.class)))),
            @ApiResponse(responseCode = "500", description = "Une erreur s'est produite", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @PostMapping("/getbyfiliere")
    public ResponseEntity<Result<List<Etudiant>>> getAllEtudiantsByFiliere(@RequestBody Filiere filiere) {
        ResponseEntity<Result<List<Etudiant>>> responseEntity;
        try {
            List<Etudiant> etudiants = (List<Etudiant>) etudiantRepository.findByFiliere(filiere);
            if (etudiants.size() == 0) {
                responseEntity = new ResponseEntity<>(new Result<>("Il n'y a aucun étudiant en " + filiere.getLibelleFiliere()), HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>(new Result<>("liste bien récupérée", etudiants), HttpStatus.OK);
            }
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(new Result<>("Une erreur s'est produite"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    /**
     * @param niveau
     * @return responseEntity
     */
    @Operation(summary = "Lister tous les étudiants en fonction du niveau", description = "", tags = {"etudiant"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Il n'y a aucun étudiant dans cette filière", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "200", description = "Liste bien récupérée", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Result.class)))),
            @ApiResponse(responseCode = "500", description = "Une erreur s'est produite", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @PostMapping("/getbyniveau")
    public ResponseEntity<Result<List<Etudiant>>> getAllEtudiantsByNiveau(@RequestBody Niveau niveau) {
        ResponseEntity<Result<List<Etudiant>>> responseEntity;
        try {
            List<Etudiant> etudiants = (List<Etudiant>) etudiantRepository.findByNiveau(niveau);
            if (etudiants.size() == 0) {
                responseEntity = new ResponseEntity<>(new Result<>("Il n'y a aucun étudiant en " + niveau.getLibelleNiveau()), HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>(new Result<>("Liste bien récupérée", etudiants), HttpStatus.OK);
            }
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(new Result<>("Une erreur s'est produite"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    /**
     * @param id
     * @return responseEntity
     */
    @Operation(summary = "supprimer un étudiant", description = "", tags = {"etudiant"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "L'étudiant a bien été supprimé", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "Une erreur s'est produite", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result<Object>> deleteEtudiant(@Parameter(description = "identifiant de l'étudiant à supprimer") @PathVariable(name = "id") Integer id) {
        ResponseEntity<Result<Object>> responseEntity;
        etudiantRepository.deleteById(id);
        Optional<Etudiant> deletedEtudiant = etudiantRepository.findById(id);
        if (deletedEtudiant.isEmpty()) {
            responseEntity = new ResponseEntity<>(new Result<>("L'étudiant a bien été supprimé"), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(new Result<>("Une erreur s'est produite"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    /**
     * @param id
     * @param etudiant
     * @return responseEntity
     */
    @Operation(summary = "Mettre à jour un étudiant", description = "", tags = {"etudiant"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "L'étudiant a bien été mis à jour", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "Une erreur s'est produite", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Result<Object>> updateEtudiant(@Parameter(description = "Identifiant de l'étudiant à mettre à jour") @PathVariable(name = "id") Integer id, @RequestBody Etudiant etudiant) {
        ResponseEntity<Result<Object>> responseEntity;
        Optional<Etudiant> temp = etudiantRepository.findById(etudiant.getIdEtudiant());
        System.out.println(temp.orElseGet(Etudiant::new));
        Etudiant etudiantToUpdate = temp.orElseGet(Etudiant::new);
        etudiantToUpdate.setNomEtudiant(etudiant.getNomEtudiant());
        etudiantToUpdate.setPrenomEtudiant(etudiant.getPrenomEtudiant());
        etudiantToUpdate.setContactEtudiant(etudiant.getContactEtudiant());
        etudiantToUpdate.setEmailEtudiant(etudiant.getEmailEtudiant());
        etudiantToUpdate.setAdresseEtudiant(etudiant.getAdresseEtudiant());
        etudiantToUpdate.setFiliere(etudiant.getFiliere());
        etudiantToUpdate.setNiveau(etudiant.getNiveau());
        etudiantRepository.save(etudiantToUpdate);
        if (etudiantToUpdate.equals(etudiantRepository.findById(etudiant.getIdEtudiant()).get())) {
            responseEntity = new ResponseEntity<>(new Result<>("L'étudiant a bien été mis à jour"), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(new Result<>("Une erreur s'est produite"), HttpStatus.OK);
        }
        return responseEntity;
    }

    @PostMapping("/sort")
    public String sort(Filiere filiere, Niveau niveau) {
        if ((filiere.getIdFiliere() == null) && (niveau.getIdNiveau() != null)) {
            return "redirect:/liste_etudiant?niveau=" + niveau.getIdNiveau();
        } else if ((filiere.getIdFiliere() != null) && (niveau.getIdNiveau() == null)) {
            return "redirect:/liste_etudiant?filiere=" + filiere.getIdFiliere();
        } else if ((filiere.getIdFiliere() != null) && (niveau.getIdNiveau() != null)) {
            return "redirect:/liste_etudiant?niveau=" + niveau.getIdNiveau() + "&filiere=" + filiere.getIdFiliere();
        } else {
            return "redirect:/liste_etudiant";
        }
    }

//    @GetMapping("/modif_page")
//    public String getModifpage(@RequestParam("id") Integer id, Model model){
//        Optional<Etudiant> e = etudiantRepository.findById(id);
//        List<Filiere> filieres = (List<Filiere>) filiereRepository.findAll();
//        List<Niveau> niveaux = (List<Niveau>) niveauRepository.findAll();
//        Etudiant etudiant = e.get();
//        model.addAttribute("etudiant", etudiant);
//        model.addAttribute("filieres", filieres.toArray());
//        model.addAttribute("niveaux", niveaux.toArray());
//        return "redirect:/modifier_etudiant";
//    }

}
