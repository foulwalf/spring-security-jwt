package com.example.crud_api.repositories;

import com.example.crud_api.models.Filiere;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FiliereRepository extends CrudRepository<Filiere, Integer> {

}
