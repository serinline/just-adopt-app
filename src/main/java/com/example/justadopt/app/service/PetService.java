package com.example.justadopt.app.service;

import com.example.justadopt.app.model.Pet;
import com.example.justadopt.app.repository.PetRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;

    public void addImage(Pet pet, MultipartFile file) throws IOException {
        pet.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        petRepository.save(pet);
    }
}
