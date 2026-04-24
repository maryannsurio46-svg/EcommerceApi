package com.ws101.surio.EcommerceApi.controller;

import com.ws101.surio.EcommerceApi.model.Product;
import com.ws101.surio.EcommerceApi.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for product-related API endpoints.
 * 
 * Exposes CRUD operations and filtering capabilities for the product catalog.
 * This controller acts as the bridge between the frontend and the service layer.
 * 
 * @author Mary Ann Surio
 * @see ProductService
 * @see Product
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    /**
     * Constructor for dependency injection.
     * 
     * @param productService the service layer for product operations
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieves all products from the catalog.
     * 
     * @return ResponseEntity containing list of all products with HTTP 200 OK status
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * Retrieves a single product by its unique identifier.
     * 
     * @param id the unique identifier of the product (must be a positive number)
     * @return ResponseEntity containing the product with HTTP 200 OK status
     * @throws com.ws101.surio.EcommerceApi.exception.ProductNotFoundException if product with given ID does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Filters products by category, price, or name.
     * 
     * Supported filter types:
     * - category: Filters by exact category match (case-insensitive)
     * - price: Filters products with price less than or equal to the given value
     * - name: Filters products with name containing the given text (case-insensitive)
     * 
     * @param filterType the type of filter ("category", "price", or "name")
     * @param filterValue the value to filter by
     * @return ResponseEntity containing filtered list of products with HTTP 200 OK status
     * @throws IllegalArgumentException if filter type is invalid or price value is not a number
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterProducts(
            @RequestParam String filterType,
            @RequestParam String filterValue) {
        return ResponseEntity.ok(productService.filterProducts(filterType, filterValue));
    }

    /**
     * Creates a new product with auto-generated ID.
     * 
     * Validates the input before creating the product:
     * - Name is required and must be at least 3 characters
     * - Price is required and must be positive
     * - Category is required
     * - Stock quantity defaults to 0 if not provided, must be non-negative
     * 
     * @param product the product to create (ID is auto-generated, do not provide)
     * @return ResponseEntity containing the created product with HTTP 201 Created status
     * @throws IllegalArgumentException if validation fails (missing name, invalid price, etc.)
     */
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
            product.setStockQuantity(0);
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

    /**
     * Replaces an entire product with new data (full update).
     * 
     * All fields must be provided as this is a full replacement operation.
     * 
     * @param id the unique identifier of the product to update
     * @param product the complete product data to replace with
     * @return ResponseEntity containing the updated product with HTTP 200 OK status
     * @throws com.ws101.surio.EcommerceApi.exception.ProductNotFoundException if product with given ID does not exist
     * @throws IllegalArgumentException if validation fails
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        
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

    /**
     * Partially updates a product (only specified fields).
     * 
     * Only the fields provided in the request body will be updated.
     * Other fields remain unchanged.
     * 
     * @param id the unique identifier of the product to update
     * @param updates a map containing only the fields to update and their new values
     * @return ResponseEntity containing the updated product with HTTP 200 OK status
     * @throws com.ws101.surio.EcommerceApi.exception.ProductNotFoundException if product with given ID does not exist
     * @throws IllegalArgumentException if validation fails for any updated field
     */
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

    /**
     * Deletes a product from the catalog by its ID.
     * 
     * @param id the unique identifier of the product to delete
     * @return ResponseEntity with HTTP 204 No Content status (no response body)
     * @throws com.ws101.surio.EcommerceApi.exception.ProductNotFoundException if product with given ID does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}