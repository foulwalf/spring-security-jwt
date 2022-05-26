package com.example.crud_api.repositories;

import com.example.crud_api.models.Niveau;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NiveauRepository extends CrudRepository<Niveau, Integer> {

}
