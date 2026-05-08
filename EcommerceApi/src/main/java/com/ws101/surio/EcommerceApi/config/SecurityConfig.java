package com.ws101.surio.EcommerceApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Import HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Keep this enabled for method-level security
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Public Endpoints (Permit All)
                .requestMatchers(HttpMethod.GET, "/api/v1/products").permitAll() // Specific to GET for products
                .requestMatchers("/api/v1/auth/register").permitAll() // POST register
                .requestMatchers("/login**", "/css/**", "/js/**", "/images/**").permitAll() // Login page, static resources

                // Require Auth: User-specific endpoints (e.g., POST /api/v1/orders)
                // Any POST or GET to /api/v1/orders needs authentication
                .requestMatchers("/api/v1/orders").authenticated()
                // You can also specify method:
                // .requestMatchers(HttpMethod.POST, "/api/v1/orders").authenticated()
                // .requestMatchers(HttpMethod.GET, "/api/v1/orders").authenticated()


                // Admin-only endpoints (DELETE /api/v1/products)
                // This explicitly says only ADMIN can DELETE products
                .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole("ADMIN")
                // Example for a specific path for admin
                // .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")


                // Default rule: Any other request needs authentication unless explicitly permitted above
                // This catches anything not explicitly allowed or restricted by role above.
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .csrf(withDefaults())
            .sessionManagement(session -> session
                .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
            );

        return http.build();
    }
}