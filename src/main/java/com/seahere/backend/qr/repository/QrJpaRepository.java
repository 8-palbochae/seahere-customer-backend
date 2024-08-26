package com.seahere.backend.qr.repository;

import com.seahere.backend.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QrJpaRepository extends JpaRepository<ProductEntity, Long> {
}
