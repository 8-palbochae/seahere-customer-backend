package com.seahere.backend.alarm.service;

import com.seahere.backend.alarm.controller.request.DiscountInventories;
import com.seahere.backend.alarm.controller.request.DiscountRequest;
import com.seahere.backend.alarm.dto.AlarmToFollowerEvent;
import com.seahere.backend.alarm.entity.DiscountEntity;
import com.seahere.backend.alarm.repository.DiscountJpaRepository;
import com.seahere.backend.company.entity.CompanyEntity;
import com.seahere.backend.company.exception.CompanyNotFound;
import com.seahere.backend.company.repository.CompanyRepository;
import com.seahere.backend.inventory.entity.InventoryDetailEntity;
import com.seahere.backend.inventory.entity.InventoryEntity;
import com.seahere.backend.inventory.repository.InventoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiscountService {

    private final InventoryJpaRepository inventoryJpaRepository;
    private final DiscountJpaRepository discountJpaRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CompanyRepository companyRepository;

    public void discountInventory(Long companyId, DiscountRequest request) {
        CompanyEntity companyEntity = companyRepository.findById(companyId).orElseThrow(CompanyNotFound::new);
        List<DiscountInventories> inventories = request.getInventories();
        StringBuilder sb = new StringBuilder();
        for(DiscountInventories inventory : inventories) {

            InventoryEntity inventoryEntity = inventoryJpaRepository.getIdWithDetails(inventory.getInventoryId());
            InventoryDetailEntity inventoryDetail = inventoryEntity.getInventoryDetail();
            inventoryDetail.changePrice(inventory.getDiscountPrice());

            if(inventoryEntity.getDiscount() != null){
                DiscountEntity discount = inventoryEntity.getDiscount();
                discount.updateDiscount(request.getStartDate(),request.getEndDate(),inventory.getDiscountPrice(),inventory.getPrice());
            }else {
                discountJpaRepository.save(DiscountEntity.builder()
                        .inventory(inventoryEntity)
                        .discountPrice(inventory.getDiscountPrice())
                        .originalPrice(inventory.getPrice())
                        .startDate(request.getStartDate())
                        .endDate(request.getEndDate())
                        .build());
            }
            sb.append(inventory.getName()).append(" ");
        }
        sb.append("세일중입니다.");
        eventPublisher.publishEvent(new AlarmToFollowerEvent(companyId,companyEntity.getCompanyName()+" 깜짝 세일중",sb.toString()));
    }
}
