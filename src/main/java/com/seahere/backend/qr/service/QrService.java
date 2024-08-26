package com.seahere.backend.qr.service;

import com.seahere.backend.product.entity.ProductEntity;
import com.seahere.backend.qr.repository.QrJpaRepository;
import com.seahere.backend.qr.repository.QrRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class QrService {

    private final QrJpaRepository qrJpaRepository;
    private final QrRepository qrRepository;

    public byte[] createQrZip(List<Long> productIds) throws Exception {
        List<ProductEntity> products = qrJpaRepository.findAllById(productIds);

        if (products.isEmpty()) {
            throw new IllegalArgumentException("Invalid product IDs: " + productIds);
        }

        return qrRepository.createQrZip(products);
    }
}