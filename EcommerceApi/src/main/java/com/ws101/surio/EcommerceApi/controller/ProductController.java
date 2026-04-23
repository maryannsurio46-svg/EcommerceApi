package com.ws101.surio.EcommerceApi.controller;

import com.ws101.surio.EcommerceApi.model.Product;
import com.ws101.surio.EcommerceApi.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterProducts(
            @RequestParam String filterType,
            @RequestParam String filterValue) {
        return ResponseEntity.ok(productService.filterProducts(filterType, filterValue));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        
        // 1. Product name validation (required, minimum length 3)
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (product.getName().length() < 3) {
            throw new IllegalArgumentException("Product name must be at least 3 characters long");
        }
        if (product.getName().length() > 100) {
            throw new IllegalArgumentException("Product name cannot exceed 100 characters");
        }

        // 2. Price validation (must be positive number)
        if (product.getPrice() == null) {
            throw new IllegalArgumentException("Product price is required");
        }
        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be positive");
        }
        if (product.getPrice() > 1000000) {
            throw new IllegalArgumentException("Product price cannot exceed 1,000,000");
        }

        // 3. Category validation (required)
        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Product category is required");
        }

        // 4. Stock quantity validation (must be non-negative)
        if (product.getStockQuantity() == null) {
            product.setStockQuantity(0); // Default to 0 if not provided
        }
        if (product.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        if (product.getStockQuantity() > 10000) {
            throw new IllegalArgumentException("Stock quantity cannot exceed 10,000");
        }

        Product createdProduct = productService.createProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        
        // Validation for update
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (product.getName().length() < 3) {
            throw new IllegalArgumentException("Product name must be at least 3 characters long");
        }
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be positive");
        }
        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Product category is required");
        }
        if (product.getStockQuantity() != null && product.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }

        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> patchProduct(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Product existing = productService.getProductById(id);
        
        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    String name = (String) value;
                    if (name == null || name.trim().isEmpty()) {
                        throw new IllegalArgumentException("Product name cannot be empty");
                    }
                    if (name.length() < 3) {
                        throw new IllegalArgumentException("Product name must be at least 3 characters long");
                    }
                    if (name.length() > 100) {
                        throw new IllegalArgumentException("Product name cannot exceed 100 characters");
                    }
                    existing.setName(name);
                    break;
                case "description":
                    existing.setDescription((String) value);
                    break;
                case "price":
                    Number priceNum = (Number) value;
                    double price = priceNum.doubleValue();
                    if (price <= 0) {
                        throw new IllegalArgumentException("Product price must be positive");
                    }
                    if (price > 1000000) {
                        throw new IllegalArgumentException("Product price cannot exceed 1,000,000");
                    }
                    existing.setPrice(price);
                    break;
                case "category":
                    String category = (String) value;
                    if (category == null || category.trim().isEmpty()) {
                        throw new IllegalArgumentException("Product category cannot be empty");
                    }
                    existing.setCategory(category);
                    break;
                case "stockQuantity":
                    Number stockNum = (Number) value;
                    int stock = stockNum.intValue();
                    if (stock < 0) {
                        throw new IllegalArgumentException("Stock quantity cannot be negative");
                    }
                    if (stock > 10000) {
                        throw new IllegalArgumentException("Stock quantity cannot exceed 10,000");
                    }
                    existing.setStockQuantity(stock);
                    break;
                case "imageUrl":
                    existing.setImageUrl((String) value);
                    break;
                default:
                    break;
            }
        });
        
        return ResponseEntity.ok(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
