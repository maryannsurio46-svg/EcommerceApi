package com.ws101.surio.EcommerceApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for the Ecommerce API.
 *
 * This class configures:
 * - Authentication rules
 * - Authorization rules
 * - Login/logout behavior
 * - Password encryption
 *
 * NOTE:
 * CSRF protection is temporarily disabled for API testing using Postman.
 * In production systems, CSRF should remain enabled.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the application's security filter chain.
     *
     * @param http HttpSecurity object used to configure web security
     * @return configured SecurityFilterChain
     * @throws Exception if security configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

            /*
             * Disable CSRF protection temporarily for Postman testing.
             * This is useful for testing APIs during development.
             */
            .csrf(csrf -> csrf.disable())

            /*
             * Configure endpoint authorization rules.
             */
            .authorizeHttpRequests(auth -> auth

                /*
                 * Public endpoints:
                 * Accessible without authentication.
                 */
                .requestMatchers("/api/auth/register").permitAll()
                .requestMatchers("/login").permitAll()

                /*
                 * Protected endpoints:
                 * Requires authenticated users.
                 */
                .requestMatchers("/api/v1/orders/**").authenticated()

                /*
                 * All remaining endpoints require authentication.
                 */
                .anyRequest().authenticated()
            )

            /*
             * Configure form-based login authentication.
             */
            .formLogin(form -> form

                /*
                 * Endpoint used to process login requests.
                 */
                .loginProcessingUrl("/login")

                /*
                 * Allow all users to access login functionality.
                 */
                .permitAll()
            )

            /*
             * Configure logout support.
             */
            .logout(logout -> logout.permitAll());

        return http.build();
    }

    /**
     * Password encoder bean.
     *
     * Uses BCrypt hashing algorithm for secure password storage.
     *
     * @return BCrypt password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}   