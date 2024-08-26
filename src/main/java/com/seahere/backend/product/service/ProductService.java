package com.seahere.backend.product.service;

import com.seahere.backend.product.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductEntity> getAllProducts();

    Optional<ProductEntity> getProduct(Long productId);
}
