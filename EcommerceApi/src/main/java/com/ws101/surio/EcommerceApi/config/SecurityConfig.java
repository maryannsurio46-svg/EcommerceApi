package com.ws101.surio.EcommerceApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults; // For simpler default configurations

@Configuration
@EnableWebSecurity // Enables Spring Security's web security support
public class SecurityConfig {

    // 1. PasswordEncoder Bean (as previously defined)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Configure the SecurityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Public Endpoints (Permit All)
                // GET requests for products
                .requestMatchers("/api/v1/products").permitAll()
                // POST request for user registration
                .requestMatchers("/api/v1/auth/register").permitAll()
                // Other public endpoints you might have (e.g., login, static resources)
                .requestMatchers("/login**", "/css/**", "/js/**", "/images/**").permitAll() // Default login page, static resources

                // Protected Endpoints (Require Authentication)
                // POST requests for orders
                .requestMatchers("/api/v1/orders").authenticated() // or .hasRole("USER") / .hasAuthority("ROLE_USER") if you have roles
                // DELETE requests for products (often restricted to ADMIN)
                .requestMatchers("/api/v1/products/**").authenticated() // .hasRole("ADMIN") or .hasAuthority("ROLE_ADMIN") would be more secure here
                // Any other request not explicitly permitted above will require authentication
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login") // Custom login page if you have one, otherwise Spring will generate one
                .loginProcessingUrl("/login") // Default URL to process the login form
                .defaultSuccessUrl("/", true) // Redirect to home page after successful login
                .failureUrl("/login?error=true") // Redirect to login page with error on failure
                .permitAll() // Allow everyone to access form login related endpoints
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // Default URL for logout
                .logoutSuccessUrl("/login?logout=true") // Redirect to login page after successful logout
                .invalidateHttpSession(true) // Invalidate HTTP session on logout
                .deleteCookies("JSESSIONID") // Delete session cookies on logout
                .permitAll() // Allow everyone to access logout functionality
            )
            .csrf(withDefaults()) // Enable CSRF protection (default behavior)
            // .csrf(csrf -> csrf.ignoringRequestMatchers("/api/v1/auth/register")) // Example: to disable CSRF for specific endpoints if necessary for APIs (use with caution)
            .sessionManagement(session -> session
                .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED) // Create session if needed (default)
                .maximumSessions(1) // Allow only one session per user
                .maxSessionsPreventsLogin(true) // Prevent new login if max sessions reached
            );

        return http.build();
    }


}