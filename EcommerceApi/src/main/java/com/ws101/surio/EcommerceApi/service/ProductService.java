package com.ws101.surio.EcommerceApi.service;

import com.ws101.surio.EcommerceApi.model.Product;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for product-related operations.
 * 
 * Provides business logic for CRUD operations, filtering, searching, and managing products.
 * This class acts as an intermediary between the API controller and the
 * in-memory data storage layer. All product data is stored in RAM using an ArrayList.
 * 
 * @author Mary Ann Surio
 * @see Product
 */
@Service
public class ProductService {

    /**
     * In-memory storage for products.
     * Uses ArrayList to store Product objects. This approach is suitable for
     * development and testing purposes. Data is lost when the application stops.
     */
    private final List<Product> productList = new ArrayList<>();
    
    /**
     * Counter for generating unique product IDs.
     * Starts at 1 and increments with each new product creation.
     * Ensures IDs remain unique across all operations.
     */
    private Long nextId = 1L;

    /**
     * Constructor initializes the service with sample data.
     * 
     * Creates and stores at least 10 sample products for testing API endpoints.
     * This demonstrates the in-memory storage approach and provides immediate
     * data for development without requiring a database connection.
     */
    public ProductService() {
        // Initialize with 10 sample products across different categories
        productList.add(new Product(nextId++, "Gaming Laptop", "High-performance laptop with RTX 4060", 1299.99, "Electronics", 10, "laptop.jpg"));
        productList.add(new Product(nextId++, "Wireless Mouse", "Ergonomic wireless mouse with RGB", 29.99, "Accessories", 50, "mouse.jpg"));
        productList.add(new Product(nextId++, "Mechanical Keyboard", "RGB mechanical gaming keyboard", 89.99, "Accessories", 30, "keyboard.jpg"));
        productList.add(new Product(nextId++, "4K Monitor", "27-inch 4K UHD monitor", 399.99, "Electronics", 15, "monitor.jpg"));
        productList.add(new Product(nextId++, "Cotton T-Shirt", "100% cotton crew neck t-shirt", 19.99, "Clothing", 100, "tshirt.jpg"));
        productList.add(new Product(nextId++, "Slim Fit Jeans", "Blue slim fit jeans", 49.99, "Clothing", 40, "jeans.jpg"));
        productList.add(new Product(nextId++, "Programming Book", "Java Programming: A Comprehensive Guide", 39.99, "Books", 25, "book.jpg"));
        productList.add(new Product(nextId++, "Noise-Cancelling Headphones", "Wireless over-ear headphones", 199.99, "Electronics", 20, "headphones.jpg"));
        productList.add(new Product(nextId++, "Ceramic Coffee Mug", "15oz ceramic coffee mug", 12.99, "Kitchen", 60, "mug.jpg"));
        productList.add(new Product(nextId++, "Laptop Backpack", "Water-resistant laptop backpack", 59.99, "Accessories", 35, "backpack.jpg"));
    }

    /**
     * Retrieves all products from the in-memory storage.
     * 
     * Returns a defensive copy of the product list to prevent external modification
     * of the internal data store. Products are returned in the order they were added.
     * 
     * @return a {@code List<Product>} containing all products in the catalog.
     *         Returns an empty list if no products are available.
     */
    public List<Product> getAllProducts() {
        return new ArrayList<>(productList);
    }

    /**
     * Finds and retrieves a product by its unique identifier.
     * 
     * Searches the in-memory product list for a product with the matching ID.
     * Uses stream filtering for efficient lookup.
     * 
     * @param id the unique identifier of the product to retrieve.
     *           Must be a positive non-null Long value.
     * @return the {@code Product} with the matching ID.
     * @throws RuntimeException if no product exists with the given ID.
     *         The exception message includes the ID that was not found.
     * 
     * @see #getAllProducts()
     */
    public Product getProductById(Long id) {
        return productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    /**
     * Creates and adds a new product to the in-memory storage.
     * 
     * Automatically generates a unique ID for the product using a counter strategy.
     * The ID is assigned before adding to the list. The nextId counter increments
     * after each creation to maintain uniqueness even after deletions.
     * 
     * @param product the product to create. The ID field can be null as it will be
     *                auto-generated. All other fields should be provided.
     * @return the created {@code Product} with the auto-generated ID assigned.
     * 
     * @see #updateProduct(Long, Product)
     * @see #deleteProduct(Long)
     */
    public Product createProduct(Product product) {
        product.setId(nextId++);
        productList.add(product);
        return product;
    }

    /**
     * Updates an existing product completely (full replacement).
     * 
     * Finds the product by ID and replaces all its fields with the values from
     * the provided updated product. This is a full update operation - all fields
     * will be overwritten.
     * 
     * @param id the unique identifier of the product to update.
     *           Must be a positive non-null Long value.
     * @param updatedProduct the product containing the new data. All fields
     *                       should be provided as this is a full replacement.
     * @return the updated {@code Product} with the new values.
     * @throws RuntimeException if no product exists with the given ID.
     * 
     * @see #createProduct(Product)
     * @see #getProductById(Long)
     */
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

    /**
     * Deletes a product from the in-memory storage by its ID.
     * 
     * Finds the product by ID and removes it from the product list.
     * After deletion, the ID is not reused for future products.
     * 
     * @param id the unique identifier of the product to delete.
     *           Must be a positive non-null Long value.
     * @throws RuntimeException if no product exists with the given ID.
     * 
     * @see #getProductById(Long)
     * @see #createProduct(Product)
     */
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productList.remove(product);
    }

    /**
     * Filters products based on specified criteria.
     * 
     * Retrieves all products that match the given filter type and value.
     * Supported filter types include category (exact match), price (maximum value),
     * and name (partial match, case-insensitive).
     * 
     * @param filterType the type of filter to apply. Must be one of:
     *                   "category" - filters by exact category match (case-insensitive),
     *                   "price" - filters products with price less than or equal to the value,
     *                   "name" - filters products with name containing the text (case-insensitive).
     * @param filterValue the value to filter by. For "category", a string category name.
     *                    For "price", a numeric value (will be parsed to double).
     *                    For "name", a string to search for in product names.
     * @return a {@code List<Product>} containing all products matching the filter criteria.
     *         Returns an empty list if no products match the criteria.
     * @throws IllegalArgumentException if filterType is not one of the supported values,
     *                                  or if filterValue for "price" cannot be parsed to a number.
     * 
     * @see #getAllProducts()
     * @see #getProductById(Long)
     */
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