package com.example.justadopt.app.controller;

import com.example.justadopt.app.model.Pet;
import com.example.justadopt.app.model.Type;
import com.example.justadopt.app.repository.PetRepository;
import com.example.justadopt.app.service.PetService;
import com.example.justadopt.payload.request.PetRequest;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pets")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PetController {

    @Autowired
    private PetRepository petRepository;
    @Autowired
    private PetService petService;

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

    @GetMapping("/all/{type}")
    public ResponseEntity<List<Pet>> getAllByType(@PathVariable("type") String type) {
        List<Pet> pets;
        try {
            if(type.equals("cat")){
                pets = petRepository.findPetsByType(Type.CAT);
            } else {
                pets = petRepository.findPetsByType(Type.DOG);
            }
            return pets.isEmpty()
                    ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(pets, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<Pet> getByName(@PathVariable("name") String name) {
        try {
            Optional<Pet> pet = petRepository.findPetByName(name);
            return pet.isEmpty()
                    ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                    : ResponseEntity.ok(pet.get());
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/photo/{name}")
    public ResponseEntity<byte[]> getPhotoOfPet(@PathVariable String name){
        Binary image = petService.getImage(name);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"")
                .body(image.getData());
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Pet> newPet(@RequestBody PetRequest pet) {
        try {
            Pet newPet = petRepository.save(Pet.builder()
                                                .name(pet.getName())
                                                .age(pet.getAge())
                                                .description(pet.getDescription())
                                                .type(pet.getType().equals("cat") ? Type.CAT : Type.DOG)
                                                .build());
            return new ResponseEntity<>(newPet, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addImage/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MultipartFile> addImage(@PathVariable String name, @RequestParam("image") MultipartFile image) {
        try {
            Optional<Pet> petData = petRepository.findPetByName(name);
            petService.addImage(petData.get(), image);
            return ResponseEntity.ok(image);
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
