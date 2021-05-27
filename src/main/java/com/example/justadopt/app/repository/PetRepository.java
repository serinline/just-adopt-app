package com.example.justadopt.app.repository;

import com.example.justadopt.app.model.Pet;
import com.example.justadopt.app.model.Type;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends MongoRepository<Pet, String> {
    void deletePetByName(String name);
    Optional<Pet> findPetByName(String name);
    List<Pet> findPetsByType(Type type);
}
