package com.ws101.surio.EcommerceApi.service;

import com.ws101.surio.EcommerceApi.model.User;
import com.ws101.surio.EcommerceApi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Locale; // For converting role to uppercase
import java.util.Optional;

@Service
public class UsersService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Modified to accept String role
    public User registerNewUser(String username, String rawPassword, String role) {
        // Business validation: Check if username already exists
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new IllegalStateException("Username '" + username + "' already exists.");
        }

        // Hash the raw password
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Convert the input role to Spring Security's expected format (e.g., "ROLE_USER")
        // and make sure it's uppercase for consistency with @Pattern validation
        String formattedRole = "ROLE_" + role.toUpperCase(Locale.ROOT);


        // Create the new User object
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(encodedPassword);
        // Assign the formatted role
        newUser.setRoles(Collections.singletonList(formattedRole));
        newUser.setEnabled(true);
        newUser.setAccountNonExpired(true);
        newUser.setAccountNonLocked(true);
        newUser.setCredentialsNonExpired(true);

        // Save the user to the database
        return userRepository.save(newUser);
    }

    // You might also have methods like updateUserPassword, etc.
    public void updateUserPassword(Long userId, String newRawPassword) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(newRawPassword));
            userRepository.save(user);
        });
    }

    // Example of retrieving a user (for other business logic, not authentication)
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}