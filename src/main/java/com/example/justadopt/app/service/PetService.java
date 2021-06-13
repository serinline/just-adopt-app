package com.example.justadopt.app.service;

import com.example.justadopt.app.model.Pet;
import com.example.justadopt.app.repository.PetRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;

    /**
     * Adds an image to exisitng Pet
     * @param pet - Pet
     * @param file - image
     * @throws IOException - because it takes MultipartFile as an parameter
     */
    public void addImage(Pet pet, MultipartFile file) throws IOException {
        pet.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        petRepository.save(pet);
    }

    /**
     * Finding an image by pet's name.
     * @param name - pet's name
     * @return Binary image
     */
    public Binary getImage(String name){
        Optional<Pet> pet = petRepository.findPetByName(name);
        return pet.get().getImage();
    }
}
