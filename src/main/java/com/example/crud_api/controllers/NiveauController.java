package com.example.crud_api.controllers;

import com.example.crud_api.models.Filiere;
import com.example.crud_api.models.Niveau;
import com.example.crud_api.models.Result;
import com.example.crud_api.repositories.NiveauRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/niveau")
@Tag(name = "niveau", description = "niveau controller")
@CrossOrigin(origins = "*")
public class NiveauController {
    @Autowired
    NiveauRepository niveauRepository;

    /**
     * @param niveau
     * @return result
     */
    @Operation(summary = "Ajouter un niveau", tags = {"niveau"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Niveau ajouté", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "Une erreur s'est produite", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @GetMapping("/add")
    public ResponseEntity<Result<Object>> addNiveau(@RequestBody Niveau niveau) {
        ResponseEntity<Result<Object>> responseEntity;
        try {
            niveauRepository.save(niveau);
            Optional<Niveau> addedNiveau = niveauRepository.findById(niveau.getIdNiveau());
            if (addedNiveau.isPresent()) {
                responseEntity = new ResponseEntity<>(new Result<>("Le niveau a bien été ajouté"), HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>(new Result<>("Une erreur s'est produite"), HttpStatus.OK);
            }
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(new Result<>("Une erreur s'est produite"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    /**
     * @param id
     * @return result
     */
    @Operation(summary = "Récupérer un niveau", tags = {"niveau"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Niveau inexistant", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "200", description = "Niveau récupéré", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "Une erreur s'est produite", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<Result<Niveau>> getNiveau(@Parameter(description = "identifiant du niveau à récupérer") @PathVariable(name = "id") Integer id) {
        ResponseEntity<Result<Niveau>> responseEntity;
        Optional<Niveau> niveau = niveauRepository.findById(id);
        if (niveau.isPresent()) {
            responseEntity = new ResponseEntity<>(new Result<>("Le niveau a bien été récupéré", niveau.get()), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>( new Result<>("Une erreur s'est produite"), HttpStatus.OK);
        }
        return responseEntity;
    }

    @Operation(summary = "Lister tous les niveaux", tags = {"niveau"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Il n'y a aucun niveau dans la base de données", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "200", description = "Niveaux récupérés", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Result.class)))),
            @ApiResponse(responseCode = "500", description = "Une erreur s'est produite", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @GetMapping("/list")
    public ResponseEntity<Result<List<Niveau>>> getNiveaux() {
        ResponseEntity<Result<List<Niveau>>> responseEntity;
        try {
            List<Niveau> niveaux = (List<Niveau>) niveauRepository.findAll();
            if (niveaux.size() == 0) {
                responseEntity = new ResponseEntity<>(new Result<>("Aucun niveau dans la base de données"), HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>(new Result<>("Niveaux bien récupérés", niveaux), HttpStatus.OK);
            }
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(new Result<>("Une erreur s'est produite"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @Operation(summary = "Mettre un niveau à jour", tags = {"niveau"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Niveau inexistant", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "200", description = "Niveau récupéré", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "Une erreur s'est produite", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @PutMapping("/update")
    public ResponseEntity<Result<Object>> updateNiveau(@RequestBody Niveau niveau) {
        ResponseEntity<Result<Object>> responseEntity;
        Optional<Niveau> temp = niveauRepository.findById(niveau.getIdNiveau());
        Niveau niveauToUpdate = temp.orElseGet(Niveau::new);
        niveauToUpdate.setLibelleNiveau(niveau.getLibelleNiveau());
        niveauRepository.save(niveauToUpdate);
        if (niveauToUpdate.equals(niveauRepository.findById(niveau.getIdNiveau()).get())) {
            responseEntity = new ResponseEntity<>(new Result<>("Le niveau a bien été modifié"), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(new Result<>("Le niveau n'a pas pu être modifié"), HttpStatus.OK);
        }
        return responseEntity;
    }

    @Operation(summary = "Supprimer un niveau", tags = {"niveau"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Niveau supprimé", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "Une erreur s'est produite", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result<Object>> deleteNiveau(@Parameter(description = "identifiant du niveau à supprimer") @PathVariable(name = "id") Integer id) {
        ResponseEntity<Result<Object>> responseEntity;
        niveauRepository.deleteById(id);
        Optional<Niveau> deletedNiveau = niveauRepository.findById(id);
        if (deletedNiveau.isEmpty()) {
            responseEntity = new ResponseEntity<>(new Result<>("Le niveau a bien été supprimé"), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(new Result<>("Le niveau n'a pas pu être supprimé"), HttpStatus.OK);
        }
        return responseEntity;
    }

}
