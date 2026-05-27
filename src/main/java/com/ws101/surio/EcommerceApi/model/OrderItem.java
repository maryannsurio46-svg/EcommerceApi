package com.ws101.surio.EcommerceApi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * OrderItem Entity
 * Represents a line item within an order (a product with quantity).
 * 
 * This entity demonstrates Many-to-One relationships with both Order and Product.
 * 
 * Relationships:
 * - Many OrderItems belong to One Order
 * - Many OrderItems can reference One Product
 * 
 * @author Mary Ann Surio
 * @see Order
 * @see Product
 */
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price; // Price at time of order (snapshot, not affected by future product price changes)

    /**
     * Many-to-One relationship with Order (Owning side)
     * FetchType.LAZY: Loads order only when accessed
     * @JoinColumn: Specifies the foreign key column name
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    /**
     * Many-to-One relationship with Product
     * FetchType.LAZY: Loads product only when accessed
     * @JoinColumn: Specifies the foreign key column name
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}