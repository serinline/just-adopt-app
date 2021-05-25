package com.example.justadopt.app.controller;

import com.example.justadopt.app.model.Pet;
import com.example.justadopt.app.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pets")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PetController {

    @Autowired
    private PetRepository petRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Pet>> getAllPets() {
        try {
            List<Pet> pets = petRepository.findAll();
            return pets.isEmpty()
                    ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(pets, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Pet> newPet(@RequestBody Pet pet) {
        try {
            Pet newPet = petRepository.save(Pet.builder()
                                                .name(pet.getName())
                                                .age(pet.getAge())
                                                .description(pet.getDescription())
                                                .type(pet.getType())
                                                .build());
            return new ResponseEntity<>(newPet, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Pet> updatePet(@PathVariable("name") String name, @RequestBody Pet pet) {
        Optional<Pet> petData = petRepository.findPetByName(name);

        if (petData.isPresent()) {
            Pet newPet = petData.get();
            String newName = pet.getName();
            if (newName != null) {
                newPet.setName(newName);
            }
            int newAge = pet.getAge();
            if (newAge != 0) {
                newPet.setAge(newAge);
            }
            String newDesc = pet.getDescription();
            if (newDesc != null) {
                newPet.setDescription(newDesc);
            }
            return new ResponseEntity<>(petRepository.save(newPet), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deletePet(@PathVariable("name") String name) {
        try {
            petRepository.deletePetByName(name);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
