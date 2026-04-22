package com.ws101.surio.EcommerceApi.service;

import com.ws101.surio.EcommerceApi.model.Product;
import com.ws101.surio.EcommerceApi.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    // In-memory storage using List<Product>
    private final List<Product> productList = new ArrayList<>();
    private Long nextId = 1L;

    // Constructor initializes with at least 10 sample products
    public ProductService() {
        // Product 1
        productList.add(new Product(nextId++, "Gaming Laptop", "High-performance laptop with RTX 4060", 1299.99, "Electronics", 10, "laptop.jpg"));
        // Product 2
        productList.add(new Product(nextId++, "Wireless Mouse", "Ergonomic wireless mouse with RGB", 29.99, "Accessories", 50, "mouse.jpg"));
        // Product 3
        productList.add(new Product(nextId++, "Mechanical Keyboard", "RGB mechanical gaming keyboard", 89.99, "Accessories", 30, "keyboard.jpg"));
        // Product 4
        productList.add(new Product(nextId++, "4K Monitor", "27-inch 4K UHD monitor", 399.99, "Electronics", 15, "monitor.jpg"));
        // Product 5
        productList.add(new Product(nextId++, "Cotton T-Shirt", "100% cotton crew neck t-shirt", 19.99, "Clothing", 100, "tshirt.jpg"));
        // Product 6
        productList.add(new Product(nextId++, "Slim Fit Jeans", "Blue slim fit jeans", 49.99, "Clothing", 40, "jeans.jpg"));
        // Product 7
        productList.add(new Product(nextId++, "Programming Book", "Java Programming: A Comprehensive Guide", 39.99, "Books", 25, "book.jpg"));
        // Product 8
        productList.add(new Product(nextId++, "Noise-Cancelling Headphones", "Wireless over-ear headphones", 199.99, "Electronics", 20, "headphones.jpg"));
        // Product 9
        productList.add(new Product(nextId++, "Ceramic Coffee Mug", "15oz ceramic coffee mug", 12.99, "Kitchen", 60, "mug.jpg"));
        // Product 10
        productList.add(new Product(nextId++, "Laptop Backpack", "Water-resistant laptop backpack", 59.99, "Accessories", 35, "backpack.jpg"));
    }

    // 1. Retrieve all products
    public List<Product> getAllProducts() {
        return new ArrayList<>(productList);
    }

    // 2. Find product by ID (throws ProductNotFoundException if not found)
    public Product getProductById(Long id) {
        return productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    // 3. Create a new product (auto-generate ID)
    public Product createProduct(Product product) {
        product.setId(nextId++);
        productList.add(product);
        return product;
    }

    // 4. Update an existing product (full replacement)
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = getProductById(id);
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setCategory(updatedProduct.getCategory());
        existing.setStockQuantity(updatedProduct.getStockQuantity());
        existing.setImageUrl(updatedProduct.getImageUrl());
        return existing;
    }

    // 5. Delete a product
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productList.remove(product);
    }

    // 6. Filter products by category, price, or name
    public List<Product> filterProducts(String filterType, String filterValue) {
        switch (filterType.toLowerCase()) {
            case "category":
                return productList.stream()
                        .filter(p -> p.getCategory().equalsIgnoreCase(filterValue))
                        .collect(Collectors.toList());
            case "price":
                try {
                    double maxPrice = Double.parseDouble(filterValue);
                    return productList.stream()
                            .filter(p -> p.getPrice() <= maxPrice)
                            .collect(Collectors.toList());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid price value: " + filterValue);
                }
            case "name":
                return productList.stream()
                        .filter(p -> p.getName().toLowerCase().contains(filterValue.toLowerCase()))
                        .collect(Collectors.toList());
            default:
                throw new IllegalArgumentException("Invalid filter type: " + filterType + ". Use 'category', 'price', or 'name'.");
        }
    }
}