package com.example.justadopt.app.controller;

import com.example.justadopt.app.model.Cat;
import com.example.justadopt.app.repository.CatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cats")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CatController {

    @Autowired
    private CatRepository catRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Cat>> getAllCats() {
        try {
            List<Cat> cats = catRepository.findAll();
            return cats.isEmpty()
                    ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(cats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Cat> newCat(@RequestBody Cat cat) {
        try {
            Cat newCat = catRepository.save(Cat.builder()
                                                .name(cat.getName())
                                                .age(cat.getAge())
                                                .description(cat.getDescription())
                                                .build());
            return new ResponseEntity<>(newCat, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit/{name}")
    public ResponseEntity<Cat> updateCat(@PathVariable("name") String name, @RequestBody Cat cat) {
        Optional<Cat> catData = catRepository.findCatByName(name);

        if (catData.isPresent()) {
            Cat newCat = catData.get();
            newCat.setName(cat.getName());
            newCat.setAge(cat.getAge());
            newCat.setDescription(cat.getDescription());
            return new ResponseEntity<>(catRepository.save(newCat), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<HttpStatus> deleteCat(@PathVariable("name") String name) {
        try {
            catRepository.deleteCatByName(name);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
