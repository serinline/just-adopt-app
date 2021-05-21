package com.example.justadopt.app.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.example.justadopt.app.model.Type.DOG;

@Document
@Data
@Builder
public class Dog {
    @Id
    private String id;
    private String name;
    private int age;
    private String description;
    private static final Type TYPE = DOG;
}
