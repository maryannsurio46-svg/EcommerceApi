package com.ws101.surio.EcommerceApi.controller;

import com.ws101.surio.EcommerceApi.dto.CreateProductRequestDto; // New Import
import com.ws101.surio.EcommerceApi.dto.ProductListingResponseDto; // New Import
import com.ws101.surio.EcommerceApi.model.Product;
import com.ws101.surio.EcommerceApi.service.ProductService;
import jakarta.validation.Valid; // New Import for @Valid
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult; // New Import

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Publicly accessible GET endpoint for all products, returning simplified DTOs
    @GetMapping
    public ResponseEntity<List<ProductListingResponseDto>> getAllProducts() {
        List<ProductListingResponseDto> products = productService.getAllProductListings(); // Use new service method
        return ResponseEntity.ok(products);
    }

    // Get product by ID - returning full entity for detailed view
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Admin or Seller can add new products using CreateProductRequestDto
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public ResponseEntity<?> addProduct(@Valid @RequestBody CreateProductRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors (e.g., return bad request with error details)
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        Product savedProduct = productService.createProduct(requestDto); // Use new service method
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    // Admin or Seller can update products using CreateProductRequestDto
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody CreateProductRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        try {
            Product updatedProduct = productService.updateProduct(id, requestDto); // Use new service method
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Admin can delete products
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product " + id + " deleted by ADMIN.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}