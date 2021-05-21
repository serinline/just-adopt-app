package com.example.justadopt.security.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
public class Role {
    @Id
    private String id;
    private Roles role;
}
