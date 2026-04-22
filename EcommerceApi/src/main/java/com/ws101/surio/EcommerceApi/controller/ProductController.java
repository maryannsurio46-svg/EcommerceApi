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
        Product createdProduct = productService.createProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> patchProduct(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Product existing = productService.getProductById(id);
        
        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    existing.setName((String) value);
                    break;
                case "description":
                    existing.setDescription((String) value);
                    break;
                case "price":
                    existing.setPrice(((Number) value).doubleValue());
                    break;
                case "category":
                    existing.setCategory((String) value);
                    break;
                case "stockQuantity":
                    existing.setStockQuantity(((Number) value).intValue());
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