package com.example.justadopt.security.controller;

import com.example.justadopt.security.config.JwtUtils;
import com.example.justadopt.security.model.Role;
import com.example.justadopt.security.model.User;
import com.example.justadopt.security.payload.request.LoginRequest;
import com.example.justadopt.security.payload.request.SignUpRequest;
import com.example.justadopt.security.payload.response.JwtResponse;
import com.example.justadopt.security.payload.response.MessageResponse;
import com.example.justadopt.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    /**
     * Authenticates user
     * @param loginUser - user to authenticate
     * @return ResponseEntity with result
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginUser) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(JwtResponse.builder()
                .token(jwtUtils.generateJwtToken(authentication))
                .email(userRepository.findByUsername(loginUser.getUsername()).get().getEmail())
                .username(loginUser.getUsername())
                .isAdmin(userRepository.findByUsername(loginUser.getUsername()).get().isAdmin())
                .build());
    }

    /**
     * adds new user into the database
     * @param signUpRequest - data of new user
     * @return ResponseEntity with result
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .isAdmin(false)
                .build();

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            roles.add(Role.ROLE_USER);
        } else {
            strRoles.forEach(role -> {
                if ("admin".equals(role)) {
                    roles.add(Role.ROLE_ADMIN);
                    roles.add(Role.ROLE_USER);
                    user.setAdmin(true);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
