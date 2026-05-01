package com.ws101.surio.EcommerceApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // ✅ Add this annotation
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> {}) // ✅ Enable CORS support
            .csrf(csrf -> csrf.disable()) // ✅ Disable CSRF for testing APIs
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/**").permitAll() // Allow API endpoints
                .anyRequest().authenticated()           // Secure everything else
            );

        return http.build();
    }
}
