package com.seahere.backend.alarm.service;

import com.seahere.backend.alarm.entity.DiscountEntity;
import com.seahere.backend.alarm.repository.DiscountJpaRepository;
import com.seahere.backend.inventory.entity.InventoryDetailEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DiscountScheduler {

    private final DiscountJpaRepository discountJpaRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void restorePrice(){
        LocalDate today = LocalDate.now();
        List<DiscountEntity> expireDiscounts = discountJpaRepository.findAllByEndDateBefore(today);
        log.info("스케줄러 실행");
        for(DiscountEntity discount : expireDiscounts){
            InventoryDetailEntity inventoryDetail = discount.getInventory().getInventoryDetail();
            inventoryDetail.changePrice(discount.getOriginalPrice());
            discountJpaRepository.delete(discount);
        }
    }
}
