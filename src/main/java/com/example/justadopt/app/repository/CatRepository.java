package com.example.justadopt.app.repository;

import com.example.justadopt.app.model.Cat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatRepository extends MongoRepository<Cat, String> {
    void deleteCatByName(String name);
    Optional<Cat> findCatByName(String name);
}
