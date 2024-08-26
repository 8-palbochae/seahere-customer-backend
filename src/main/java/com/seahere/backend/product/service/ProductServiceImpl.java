package com.seahere.backend.product.service;

import com.seahere.backend.product.entity.ProductEntity;
import com.seahere.backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public List<ProductEntity> getAllProducts() {
        return new ArrayList<>(productRepository.findAll());
    }

    @Override
    public Optional<ProductEntity> getProduct(Long productId) {
        return productRepository.findById(productId);
    }
}
