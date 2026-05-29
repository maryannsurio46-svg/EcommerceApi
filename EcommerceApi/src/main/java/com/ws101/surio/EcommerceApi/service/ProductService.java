package com.ws101.surio.EcommerceApi.service;

import com.ws101.surio.EcommerceApi.dto.CreateProductRequestDto;
import com.ws101.surio.EcommerceApi.dto.ProductListingResponseDto;
import com.ws101.surio.EcommerceApi.model.Category;
import com.ws101.surio.EcommerceApi.model.Product;
import com.ws101.surio.EcommerceApi.repository.CategoryRepository;
import com.ws101.surio.EcommerceApi.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductListingResponseDto> getAllProductListings() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToProductListingResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(CreateProductRequestDto dto) {

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Category not found with ID: " + dto.getCategoryId()));

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setImageUrl(dto.getImageUrl());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setProductCategory(category);

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, CreateProductRequestDto dto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Product not found with ID: " + id));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Category not found with ID: " + dto.getCategoryId()));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setImageUrl(dto.getImageUrl());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setProductCategory(category);

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductListingResponseDto mapToProductListingResponseDto(Product product) {
        return new ProductListingResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }
}