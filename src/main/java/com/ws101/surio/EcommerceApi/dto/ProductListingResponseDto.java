package com.ws101.surio.EcommerceApi.dto;

// Using a Java Record for a concise immutable DTO
public record ProductListingResponseDto(
    Long prodId,
    String prodName,
    double prodPrice
) {}
