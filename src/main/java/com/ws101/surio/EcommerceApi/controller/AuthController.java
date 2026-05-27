package com.ws101.surio.EcommerceApi.controller;

import com.ws101.surio.EcommerceApi.model.User;
import com.ws101.surio.EcommerceApi.service.UsersService;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsersService usersService;

    public AuthController(UsersService usersService) {
        this.usersService = usersService;
    }

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
}