package com.ws101.surio.EcommerceApi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Import for @PreAuthorize
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    // Example Product Service (you'll implement real service later)
    // @Autowired
    // private ProductService productService;

    // Publicly accessible (as per SecurityConfig)
    @GetMapping
    public ResponseEntity<List<String>> getAllProducts() {
        // In a real app, this would fetch from a service
        List<String> products = new ArrayList<>();
        products.add("Laptop");
        products.add("Mouse");
        products.add("Keyboard");
        return ResponseEntity.ok(products);
    }

    // This endpoint will require ADMIN role.
    // SecurityConfig already permits all for /api/v1/products GET, but the DELETE
    // on this path needs specific role.
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only users with ROLE_ADMIN can delete products
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        // Call service to delete product
        System.out.println("Deleting product with ID: " + id);
        return ResponseEntity.ok("Product " + id + " deleted by ADMIN.");
    }

    // This endpoint could also be admin-only or restricted based on business logic
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')") // Example: ADMIN or SELLER can add
    public ResponseEntity<String> addProduct(@RequestBody String productName) {
        System.out.println("Adding product: " + productName);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product " + productName + " added.");
    }
}