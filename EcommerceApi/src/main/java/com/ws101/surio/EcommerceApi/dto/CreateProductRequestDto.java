package com.ws101.surio.EcommerceApi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // Lombok: Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: Generates no-argument constructor
@AllArgsConstructor // Lombok: Generates constructor with all fields
public class CreateProductRequestDto {

    @NotBlank(message = "Product name cannot be empty")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Category cannot be empty")
    private String category;

    // Description can be null, but if present, let's set a size constraint
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private String imageUrl; // Image URL might be optional or handled separately

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be a positive value")
    private Double price;

    @NotNull(message = "Stock quantity cannot be null")
    @Positive(message = "Stock quantity must be a positive value")
    private Integer stockQuantity;

    // Assuming category_id is required
    @NotNull(message = "Category ID cannot be null")
    @Positive(message = "Category ID must be a positive value")
    private Long categoryId;
}