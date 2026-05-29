package com.ws101.surio.EcommerceApi.controller;

import com.ws101.surio.EcommerceApi.service.CustomUserDetailsService;
import com.ws101.surio.EcommerceApi.model.User;
import com.ws101.surio.EcommerceApi.security.JwtUtil;
import com.ws101.surio.EcommerceApi.service.UsersService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsersService usersService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(
            UsersService usersService,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            CustomUserDetailsService userDetailsService
    ) {
        this.usersService = usersService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // ======================
    // REGISTER
    // ======================
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(List.of("ROLE_USER"));
        }

        User savedUser = usersService.registerNewUser(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().get(0).replace("ROLE_", "")
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // ======================
    // LOGIN (JWT)
    // ======================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails user =
                userDetailsService.loadUserByUsername(request.getUsername());

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(Map.of(
                "token", token
        ));
    }
}