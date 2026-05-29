package com.ws101.surio.EcommerceApi.service;

import com.ws101.surio.EcommerceApi.model.User;
import com.ws101.surio.EcommerceApi.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

@Service
public class UsersService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTER USER
    public User registerNewUser(
            String username,
            String rawPassword,
            String role
    ) {

        // Check if username already exists
        Optional<User> existingUser =
                userRepository.findByUsername(username);

        if (existingUser.isPresent()) {
            throw new IllegalStateException(
                    "Username '" + username + "' already exists."
            );
        }

        // Encode password
        String encodedPassword =
                passwordEncoder.encode(rawPassword);

        // Format role
        String formattedRole =
                "ROLE_" + role.toUpperCase(Locale.ROOT);

        // Create user
        User newUser = new User();

        newUser.setUsername(username);
        newUser.setPassword(encodedPassword);

        // Assign role
        newUser.setRoles(
                Collections.singletonList(formattedRole)
        );

        // Save user
        return userRepository.save(newUser);
    }

    // FIND USER
    public User findUserByUsername(String username) {

        return userRepository.findByUsername(username)
                .orElse(null);
    }

    // UPDATE PASSWORD
    public void updateUserPassword(
            Long userId,
            String newRawPassword
    ) {

        userRepository.findById(userId).ifPresent(user -> {

            user.setPassword(
                    passwordEncoder.encode(newRawPassword)
            );

            userRepository.save(user);
        });
    }
}