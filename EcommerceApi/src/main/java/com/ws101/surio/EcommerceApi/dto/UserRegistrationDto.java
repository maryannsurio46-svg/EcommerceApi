package com.ws101.surio.EcommerceApi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern; // Import Pattern for role validation
import jakarta.validation.constraints.Size;

public class UserRegistrationDto {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    // Consider if username should be unique (handled in service) or a valid email
    @Email(message = "Username should be a valid email address")
    
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Role cannot be empty")
    // Validate role input: must be either "USER", "SELLER", or "ADMIN"
    // Use a regular expression to enforce specific allowed roles.
    @Pattern(regexp = "USER|SELLER|ADMIN", message = "Role must be USER, SELLER, or ADMIN")
    private String role; // New field for role

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() { // New getter for role
        return role;
    }

    public void setRole(String role) { // New setter for role
        this.role = role;
    }
}