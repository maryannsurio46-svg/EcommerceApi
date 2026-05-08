package com.ws101.surio.EcommerceApi.service;

import com.ws101.surio.EcommerceApi.dto.CreateProductRequestDto;
import com.ws101.surio.EcommerceApi.dto.ProductListingResponseDto;
import com.ws101.surio.EcommerceApi.model.Category; // NEW IMPORT
import com.ws101.surio.EcommerceApi.model.Product;
import com.ws101.surio.EcommerceApi.repository.CategoryRepository; // NEW IMPORT
import com.ws101.surio.EcommerceApi.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository; // NEW: Inject CategoryRepository

    // Update constructor to inject CategoryRepository
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository; // Initialize it
    }

    // --- Method for line 31 error ---
    public List<ProductListingResponseDto> getAllProductListings() {
        return productRepository.findAll().stream()
                .map(this::mapToProductListingResponseDto) // Map each Product to DTO
                .collect(Collectors.toList());
    }

    // --- Method for line 38 error ---
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // --- Method for line 76 error ---
    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Product with ID " + id + " not found for deletion.");
        }
    }

    // ... (createProduct, updateProduct, and mapper methods below) ...

    public Product createProduct(CreateProductRequestDto requestDto) {
        Product product = mapToProductEntity(requestDto);
        Category category = categoryRepository.findById(requestDto.getCategoryId())
                                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + requestDto.getCategoryId()));
        product.setProductCategory(category);
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, CreateProductRequestDto requestDto) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product productToUpdate = existingProduct.get();
            productToUpdate.setName(requestDto.getName());
            productToUpdate.setCategory(requestDto.getCategory());
            productToUpdate.setDescription(requestDto.getDescription());
            productToUpdate.setImageUrl(requestDto.getImageUrl());
            productToUpdate.setPrice(requestDto.getPrice());
            productToUpdate.setStockQuantity(requestDto.getStockQuantity());
            Category category = categoryRepository.findById(requestDto.getCategoryId())
                                    .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + requestDto.getCategoryId()));
            productToUpdate.setProductCategory(category);
            return productRepository.save(productToUpdate);
        } else {
            throw new IllegalArgumentException("Product with ID " + id + " not found for update.");
        }
    }

    private Product mapToProductEntity(CreateProductRequestDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setDescription(dto.getDescription());
        product.setImageUrl(dto.getImageUrl());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        return product;
    }

    private ProductListingResponseDto mapToProductListingResponseDto(Product product) {
        return new ProductListingResponseDto(
            product.getId(),
            product.getName(),
            product.getPrice()
        );
    }
}