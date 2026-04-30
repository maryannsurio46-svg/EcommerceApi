package com.ws101.surio.EcommerceApi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order Entity
 * Represents a customer order in the e-commerce system.
 * 
 * This entity demonstrates a One-to-Many relationship with OrderItem.
 * One Order can have multiple OrderItems.
 * 
 * Relationship: Order (1) ──────< 👎 OrderItem
 * 
 * @author Mary Ann Surio
 * @see OrderItem
 * @see Product
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private String status; // PENDING, COMPLETED, CANCELLED

    /**
     * One-to-Many relationship with OrderItem
     * mappedBy: Refers to the "order" field in OrderItem entity
     * CascadeType.ALL: All operations cascade to child order items
     * FetchType.LAZY: Loads order items only when accessed
     * orphanRemoval: Removes child if parent removes reference
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * Helper method to add an order item
     * Maintains both sides of the bidirectional relationship
     * 
     * @param item the order item to add
     */
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
        calculateTotalAmount();
    }

    /**
     * Helper method to remove an order item
     * Maintains both sides of the bidirectional relationship
     * 
     * @param item the order item to remove
     */
    public void removeOrderItem(OrderItem item) {
        orderItems.remove(item);
        item.setOrder(null);
        calculateTotalAmount();
    }

    /**
     * Calculates total amount based on all order items
     * Sum = quantity * price for each order item
     */
    public void calculateTotalAmount() {
        this.totalAmount = orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}