package com.seahere.backend.inventory.service;

import com.seahere.backend.inventory.controller.request.InventoryEditReq;
import com.seahere.backend.inventory.entity.InventoryDetailEntity;
import com.seahere.backend.inventory.exception.InventoryNotFoundException;
import com.seahere.backend.inventory.repository.InventoryJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Sql(value = "/sql/inventory-service-test.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Slf4j
@SpringBootTest
class InventoryDetailServiceTest{
    @Autowired
    InventoryDetailService inventoryDetailService;

    @Autowired
    InventoryJpaRepository inventoryJpaRepository;

    @Disabled
    @Transactional
    @Test
    @DisplayName("재고 상세 정보는 수정이 가능하다.")
    void test2() throws Exception {
        //given
        Long inventoryId = 1L;
        InventoryEditReq inventoryEditReq = InventoryEditReq.builder()
                .outgoingPrice(BigDecimal.valueOf(50000))
                .warningQuantity(10).build();
        //when
        inventoryDetailService.editInventoryDetail(inventoryId,inventoryEditReq);

        InventoryDetailEntity result = inventoryJpaRepository.findById(inventoryId)
                .orElseThrow(InventoryNotFoundException::new)
                .getInventoryDetail();
        //then
        Assertions.assertEquals(result.getOutgoingPrice(),BigDecimal.valueOf(50000));
        Assertions.assertEquals(result.getWarningQuantity(),10);
    }
}