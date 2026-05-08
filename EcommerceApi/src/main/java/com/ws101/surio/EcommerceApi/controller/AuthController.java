package com.ws101.surio.EcommerceApi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

@RestController
public class AuthController {

    // ✅ Endpoint to check if user is logged in
    @GetMapping("/api/auth/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // Return username or user details
        return ResponseEntity.ok("Logged in as: " + principal.getName());
    }
}