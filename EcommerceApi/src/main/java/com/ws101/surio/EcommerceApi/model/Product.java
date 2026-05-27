package com.ws101.surio.EcommerceApi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Product Entity
 * Represents a product in the e-commerce catalog.
 * 
 * This entity maps to the "products" table in the database.
 * It has a Many-to-One relationship with Category.
 * 
 * @author Mary Ann Surio
 * @see Category
 * @see OrderItem
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String category;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "image_url")
    private String imageUrl;

    /**
     * Many-to-One relationship with Category
     * FetchType.LAZY: Loads category only when accessed (performance)
     * @JoinColumn: Specifies the foreign key column name
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category productCategory;
}