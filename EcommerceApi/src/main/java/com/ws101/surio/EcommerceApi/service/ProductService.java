package com.ws101.surio.EcommerceApi.service;

import com.ws101.surio.EcommerceApi.model.Product;
import com.ws101.surio.EcommerceApi.repository.ProductRepository;
import com.ws101.surio.EcommerceApi.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Retrieve all products from database
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Find product by ID from database
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    // Create a new product in database
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // Update an existing product in database
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = getProductById(id);
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setCategory(updatedProduct.getCategory());
        existing.setStockQuantity(updatedProduct.getStockQuantity());
        existing.setImageUrl(updatedProduct.getImageUrl());
        return productRepository.save(existing);
    }

    // Delete a product from database
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    // Filter products by category, price, or name
    public List<Product> filterProducts(String filterType, String filterValue) {
        switch (filterType.toLowerCase()) {
            case "category":
                return productRepository.findByCategoryIgnoreCase(filterValue);
            case "price":
                try {
                    double maxPrice = Double.parseDouble(filterValue);
                    return productRepository.findByPriceBetween(0.0, maxPrice);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid price value: " + filterValue);
                }
            case "name":
                return productRepository.findByNameContainingIgnoreCase(filterValue);
            default:
                throw new IllegalArgumentException("Invalid filter type: " + filterType + ". Use 'category', 'price', or 'name'.");
        }
    }
}