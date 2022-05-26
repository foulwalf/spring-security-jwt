package com.example.crud_api.controllers;

import com.example.crud_api.models.Filiere;
import com.example.crud_api.models.Result;
import com.example.crud_api.repositories.FiliereRepository;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/filiere")
@Tag(name = "filiere", description = "filiere controller")
@CrossOrigin(origins = "*")
public class FiliereController {
    @Autowired
    FiliereRepository filiereRepository;

    /**
     * @param filiere
     * @return result
     */
    @Operation(summary = "Ajouter une filière", tags = {"filiere"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "La filière à bien été ajoutée", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "Une erreur s'est produite", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @PostMapping("/add")
    public ResponseEntity<Result<Object>> addFiliere(@RequestBody Filiere filiere) {
        ResponseEntity<Result<Object>> responseEntity;
        filiereRepository.save(filiere);
        Optional<Filiere> addedFiliere = filiereRepository.findById(filiere.getIdFiliere());
        if (addedFiliere.isPresent()) {
            responseEntity = new ResponseEntity<>(new Result<>("La filière à bien été ajoutée"), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(new Result<>("Une erreur s'est produite"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    /**
     * @return result
     */
    @Operation(summary = "Lister toutes les filières", tags = {"filiere"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Il n'y a aucune filière dans la base de données", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "200", description = "Liste bien récupérée", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "Une erreur s'est produite", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @GetMapping("/list")
    public ResponseEntity<Result<List<Filiere>>> getAllFilieres() {
        ResponseEntity<Result<List<Filiere>>> responseEntity;
        try {
            List<Filiere> filieres = (List<Filiere>) filiereRepository.findAll();
            if (filieres.size() == 0) {
                responseEntity = new ResponseEntity<>(new Result<>("Il n'y a aucune filière dans la base de données"), HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>(new Result<>("Filière bien récupérées", filieres), HttpStatus.OK);
            }
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(new Result<>("Une erreur s'est produite"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @Operation(summary = "Récupérer une filière", tags = {"filiere"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filière inexistante", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "200", description = "Filière récupérée", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "Une erreur s'est produite", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<Result<Filiere>> getFiliere(@PathVariable(name = "id") Integer id) {
        ResponseEntity<Result<Filiere>> responseEntity;
        try {
            Optional<Filiere> filiere = filiereRepository.findById(id);
            if (filiere.isEmpty()) {
                responseEntity = new ResponseEntity<>(new Result<>("Filière inexistante"), HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>(new Result<>("Filière récupérée", filiere.get()), HttpStatus.OK);
            }
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(new Result<>("Une erreur s'est produite"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @Operation(summary = "Mettre à jour une filière", tags = {"filiere"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filière modifiée", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "Une erreur s'est produite", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @PutMapping("/update")
    public ResponseEntity<Result<Object>> updateFiliere(@RequestBody Filiere filiere) {
        ResponseEntity<Result<Object>> responseEntity;
        Optional<Filiere> temp = filiereRepository.findById(filiere.getIdFiliere());
        Filiere filiereToUpdate = temp.orElseGet(Filiere::new);
        filiereToUpdate.setLibelleFiliere(filiere.getLibelleFiliere());
        filiereRepository.save(filiereToUpdate);
        if (filiereToUpdate.equals(filiereRepository.findById(filiere.getIdFiliere()).get())) {
            responseEntity = new ResponseEntity<>(new Result<>("La filière a bien été mise à jour"), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(new Result<>("Une erreur s'est produite"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @Operation(summary = "Supprimer une filière", tags = {"filiere"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filière supprimée", content = @Content(schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "Une erreur s'est produite", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result<Object>> deleteFiliere(@Parameter(description = "identifiant de la filière à supprimer") @PathVariable(name = "id") Integer id) {
        ResponseEntity<Result<Object>> responseEntity;
        filiereRepository.deleteById(id);
        Optional<Filiere> deletedFiliere = filiereRepository.findById(id);
        if (deletedFiliere.isEmpty()) {
            responseEntity = new ResponseEntity<>(new Result<>("La filière a bien été supprimée"), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(new Result<>("Une erreur s'est produite"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}
