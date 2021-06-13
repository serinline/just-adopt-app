package com.example.justadopt.payload.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class PetRequest {
    @NotBlank
    private String name;
    @NotBlank
    private int age;
    @NotBlank
    private String description;
    @NotBlank
    private String type;
}
