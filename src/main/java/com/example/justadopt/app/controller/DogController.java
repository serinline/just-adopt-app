package com.example.justadopt.app.controller;

import com.example.justadopt.app.model.Dog;
import com.example.justadopt.app.repository.DogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dogs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DogController {

    @Autowired
    private DogRepository dogRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Dog>> getAllDogs() {
        try {
            List<Dog> dogs = dogRepository.findAll();
            return dogs.isEmpty()
                    ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(dogs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Dog> newDog(@RequestBody Dog dog) {
        try {
            Dog newDog = dogRepository.save(Dog.builder()
                    .name(dog.getName())
                    .age(dog.getAge())
                    .description(dog.getDescription())
                    .build());
            return new ResponseEntity<>(newDog, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit/{name}")
    public ResponseEntity<Dog> updateDog(@PathVariable("name") String name, @RequestBody Dog dog) {
        Optional<Dog> dogData = dogRepository.findDogByName(name);

        if (dogData.isPresent()) {
            Dog newDog = dogData.get();
            newDog.setName(dog.getName());
            newDog.setAge(dog.getAge());
            newDog.setDescription(dog.getDescription());
            return new ResponseEntity<>(dogRepository.save(newDog), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<HttpStatus> deleteDog(@PathVariable("name") String name) {
        try {
            dogRepository.deleteDogByName(name);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}