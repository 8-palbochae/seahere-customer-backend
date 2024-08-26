package com.seahere.backend.outgoing.controller.request;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class OutgoingCreateDetailReq {
    private Long inventoryId;
    private Float quantity;
    private BigDecimal price;

    @Builder
    public OutgoingCreateDetailReq(Long inventoryId, Float quantity, BigDecimal price) {
        this.inventoryId = inventoryId;
        this.quantity = quantity;
        this.price = price != null ? price : BigDecimal.valueOf(0);
    }
}
