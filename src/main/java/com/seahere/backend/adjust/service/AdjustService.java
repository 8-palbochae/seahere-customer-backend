package com.seahere.backend.adjust.service;

import com.seahere.backend.adjust.controller.request.AdjustRequest;
import com.seahere.backend.adjust.entity.AdjustEntity;
import com.seahere.backend.adjust.repository.AdjustJpaRepository;
import com.seahere.backend.inventory.entity.InventoryEntity;
import com.seahere.backend.inventory.repository.InventoryJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdjustService {
    private final AdjustJpaRepository adjustJpaRepository;
    private final InventoryJpaRepository inventoryJpaRepository;

    public void save(AdjustRequest adjustRequest) {
        try {
            log.info("Adjust request: {}", adjustRequest);


            if (adjustRequest.getInventoryId() == null) {
                log.error("Inventory ID must not be null");
                throw new IllegalArgumentException("Inventory ID must not be null");
            }

            InventoryEntity inventoryEntity = inventoryJpaRepository.findById(adjustRequest.getInventoryId()).orElse(null);

            if (inventoryEntity == null) {
                log.error("Inventory not found for id: {}", adjustRequest.getInventoryId());
                throw new IllegalArgumentException("Inventory not found");
            }

            log.info("Inventory found: {}", inventoryEntity);

            AdjustEntity adjustEntity = adjustRequest.toEntity(inventoryEntity);
            inventoryEntity.updateQuantity(adjustRequest.getAfterQuantity());
            adjustJpaRepository.save(adjustEntity);
            log.info("Adjust entity saved: {}", adjustEntity);
        } catch (Exception e) {
            log.error("Error saving adjust request: {}", e.getMessage(), e);
            throw e;
        }
    }
}
