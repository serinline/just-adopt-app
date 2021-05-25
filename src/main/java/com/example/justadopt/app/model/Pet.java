package com.example.justadopt.app.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
public class Pet {
    @Id
    private String id;
    private String name;
    private int age;
    private String description;
    private final Type type;
}