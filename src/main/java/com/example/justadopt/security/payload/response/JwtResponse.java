package com.example.justadopt.security.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class JwtResponse {
    private static final String TYPE = "Bearer";

    private String token;
    private String username;
    private String email;
    private boolean isAdmin;
}
