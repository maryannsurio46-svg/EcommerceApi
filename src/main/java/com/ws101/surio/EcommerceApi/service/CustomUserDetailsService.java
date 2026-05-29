package com.ws101.surio.EcommerceApi.service;

import com.ws101.surio.EcommerceApi.model.User;
import com.ws101.surio.EcommerceApi.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username)
                );

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(
                        user.getRoles()
                                .stream()
                                .map(r -> r.replace("ROLE_", ""))
                                .toArray(String[]::new)
                )
                .build();
    }
}