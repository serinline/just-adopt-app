package com.example.justadopt.app;

import com.example.justadopt.app.controller.PetController;
import com.example.justadopt.app.model.Pet;
import com.example.justadopt.app.repository.PetRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTest {

    @Mock
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
}
