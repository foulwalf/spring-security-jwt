package com.example.crud_api.repositories;

import com.example.crud_api.models.Etudiant;
import com.example.crud_api.models.Filiere;
import com.example.crud_api.models.Niveau;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtudiantRepository extends CrudRepository<Etudiant, Integer> {
    @Query("SELECT e FROM etudiant e WHERE e.filiere = :filiere")
    List<Etudiant> findByFiliere(@Param("filiere") Filiere filiere);
    @Query("SELECT e FROM etudiant e WHERE e.niveau = :niveau")
    List<Etudiant> findByNiveau(@Param("niveau") Niveau niveau);
    @Query("SELECT e FROM etudiant e WHERE e.niveau = :niveau and e.filiere = :filiere")
    List<Etudiant> findByFiliereAndNiveau(@Param("filiere") Filiere filiere, @Param("niveau") Niveau niveau);
}
