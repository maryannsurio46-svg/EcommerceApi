package com.ws101.surio.EcommerceApi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Category Entity
 * Represents a product category (e.g., Electronics, Clothing, Books).
 * 
 * This entity demonstrates a One-to-Many relationship with Product.
 * One Category can have many Products.
 * 
 * Relationship: Category (1) ──────< 👎 Product
 * 
 * @author Mary Ann Surio
 * @see Product
 */
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 200)
    private String description;

    /**
     * One-to-Many relationship with Product
     * mappedBy: Refers to the "productCategory" field in Product entity (the owning side)
     * CascadeType.ALL: All operations (save, delete, update) cascade to child products
     * FetchType.LAZY: Loads products only when accessed (improves performance)
     */
    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    /**
     * Helper method to add a product to category
     * Maintains both sides of the bidirectional relationship
     * 
     * @param product the product to add
     */
    public void addProduct(Product product) {
        products.add(product);
        product.setProductCategory(this);
    }

    /**
     * Helper method to remove a product from category
     * Maintains both sides of the bidirectional relationship
     * 
     * @param product the product to remove
     */
    public void removeProduct(Product product) {
        products.remove(product);
        product.setProductCategory(null);
    }
}