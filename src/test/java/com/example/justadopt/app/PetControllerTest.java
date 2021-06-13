package com.example.justadopt.app;

import com.example.justadopt.app.controller.PetController;
import com.example.justadopt.app.model.Pet;
import com.example.justadopt.app.model.Type;
import com.example.justadopt.app.repository.PetRepository;
import com.example.justadopt.payload.request.PetRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTest {

    private static final String ID = "Id";
    private static final String NAME = "Name";
    private static final String DESC = "desciption";
    private static final int AGE = 5;
    
    @MockBean
    PetRepository petRepository;

    @Autowired
    PetController petController;

    @ParameterizedTest
    @MethodSource
    void shouldFindAllPets(List<Pet> pets, ResponseEntity response){
        // given
        when(petRepository.findAll()).thenReturn(pets);

        // when
        ResponseEntity<List<Pet>> result = petController.getAllPets();

        // then
        assertThat(result).isEqualTo(response);
    }

    private static Stream<Arguments> shouldFindAllPets() {
        List<Pet> expected = List.of(Pet.builder().build());
        return Stream.of(
                arguments(expected, ResponseEntity.ok(expected)),
                arguments(Collections.emptyList(), new ResponseEntity<>(HttpStatus.NO_CONTENT))
        );
    }

    @ParameterizedTest
    @MethodSource
    void shouldFindAllPetsWithType(Type type, List<Pet> expected, int size){
        // given
        when(petRepository.findPetsByType(type)).thenReturn(expected);

        // when
        ResponseEntity<List<Pet>> result = petController.getAllByType(type.name());

        // then
        assertThat(result.getBody().size()).isEqualTo(size);
    }

    private static Stream<Arguments> shouldFindAllPetsWithType() {
        return Stream.of(
                arguments(Type.CAT, List.of(Pet.builder().type(Type.CAT).build()), 1),
                arguments(Type.DOG, List.of(Pet.builder().type(Type.DOG).build(),
                        Pet.builder().type(Type.DOG).build()), 2)
        );
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    void shouldAddNewPet(){
        // given
        Pet pet = createPet();
        when(petRepository.save(isA(Pet.class))).thenReturn(pet);

        // when
        ResponseEntity<Pet> result = petController.newPet(
                PetRequest.builder().name(NAME).age(AGE).description(DESC).type("dog").build());

        // then
        assertThat(result.getBody()).isEqualTo(pet);
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    void shouldUpdateExistingPet() {
        // given
        Pet pet = createPet();
        Pet newPet = Pet.builder().age(2).description(DESC).name(NAME).type(Type.DOG).build();
        when(petRepository.findPetByName(any(String.class))).thenReturn(Optional.ofNullable(pet));
        when(petRepository.save(isA(Pet.class))).thenReturn(newPet);

        // when
        ResponseEntity<Pet> result = petController.updatePet(NAME, newPet);

        // then
        assertThat(Objects.requireNonNull(result.getBody()).getAge()).isEqualTo(2);
        assertThat(Objects.requireNonNull(result.getBody()).getName()).isEqualTo(NAME);
        assertThat(Objects.requireNonNull(result.getBody()).getDescription()).isEqualTo(DESC);
        assertThat(Objects.requireNonNull(result.getBody()).getType()).isEqualTo(Type.DOG);
    }

    private Pet createPet(){
        return Pet.builder().age(AGE).description(DESC).name(NAME).type(Type.DOG).build();
    }
}
