package com.ws101.surio.EcommerceApi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Import for @PreAuthorize
import org.springframework.web.bind.annotation.*;

import java.security.Principal; // To get the authenticated user's name

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    // This endpoint will require any authenticated user (e.g., USER, SELLER, ADMIN).
    // The SecurityConfig now defaults to .anyRequest().authenticated(), so this is a bit redundant
    // for just 'authenticated()'. However, it's good for more complex expressions.
    @PostMapping
    @PreAuthorize("isAuthenticated()") // Any authenticated user can create an order
    public ResponseEntity<String> createOrder(@RequestBody String orderDetails, Principal principal) {
        // In a real app, you'd process the order details with a service
        String username = (principal != null) ? principal.getName() : "Unknown User";
        System.out.println("Order created by: " + username + " with details: " + orderDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully by " + username + ".");
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()") // Any authenticated user can view their orders
    public ResponseEntity<String> getUserOrders(Principal principal) {
        String username = (principal != null) ? principal.getName() : "Unknown User";
        return ResponseEntity.ok("Orders for user: " + username);
    }
}