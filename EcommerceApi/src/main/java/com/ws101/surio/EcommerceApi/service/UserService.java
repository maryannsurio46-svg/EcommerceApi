package com.ws101.surio.EcommerceApi.service;

import com.ws101.surio.EcommerceApi.model.User;
import com.ws101.surio.EcommerceApi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections; // For creating a simple roles list

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Inject the PasswordEncoder

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(String username, String rawPassword) {
        // 1. Hash the raw password before saving
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 2. Create the new User object
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(encodedPassword);
        // Assign default roles, e.g., "ROLE_USER"
        newUser.setRoles(Collections.singletonList("ROLE_USER"));
        newUser.setEnabled(true);
        newUser.setAccountNonExpired(true);
        newUser.setAccountNonLocked(true);
        newUser.setCredentialsNonExpired(true);

        // 3. Save the user to the database
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
