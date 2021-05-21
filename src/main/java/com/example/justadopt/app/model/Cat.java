package com.example.justadopt.app.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.example.justadopt.app.model.Type.CAT;

@Document
@Data
@Builder
public class Cat {
    @Id
    private String id;
    private String name;
    private int age;
    private String description;
    private static final Type TYPE = CAT;
}