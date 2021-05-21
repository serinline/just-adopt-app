package com.example.justadopt.app.repository;

import com.example.justadopt.app.model.Dog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DogRepository extends MongoRepository<Dog, String> {
    void deleteDogByName(String name);
    Optional<Dog> findDogByName(String name);
}
